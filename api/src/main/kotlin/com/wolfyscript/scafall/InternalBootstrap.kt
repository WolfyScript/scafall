package com.wolfyscript.scafall

import com.wolfyscript.scafall.loader.PluginBootstrap
import com.wolfyscript.scafall.loader.ScafallBootstrap
import com.wolfyscript.scafall.loader.ScafallModule
import org.jetbrains.annotations.ApiStatus.Internal
import java.lang.reflect.Constructor
import java.util.function.Consumer

/**
 * Creates internal bootstrap classes that are linked to this API module.
 *
 */
@Internal
internal class InternalBootstrap : ScafallBootstrap {

    override fun createScaffoldingModule(entrypoint: String, loader: Any): ScafallModule {
        val moduleClass: Class<out ScafallModule> = try {
            javaClass.classLoader.loadClass(entrypoint).asSubclass(ScafallModule::class.java)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException("Unable to load module: $entrypoint", e)
        }

        val constructor: Constructor<out ScafallModule> = try {
            moduleClass.getConstructor(loader.javaClass, Scafall::class.java)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException("Unable to find constructor for module: $entrypoint", e)
        }

        val module: ScafallModule = try {
            constructor.newInstance(loader, ScafallProvider.get())
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException(e)
        }
        return module
    }

    override fun initScaffoldingPlatform(pathToBootstrap: String, loaderType: Class<*>, loader: Any): PluginBootstrap {
        if (ScafallProvider.registered()) {
            throw IllegalStateException("Scaffolding Platform is already initialized!")
        }

        val plugin: Class<out PluginBootstrap> = try {
            javaClass.classLoader.loadClass(pathToBootstrap).asSubclass(PluginBootstrap::class.java)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Unable to load plugin bootstrap class $pathToBootstrap", e)
        }

        val constructor: Constructor<out PluginBootstrap> = try {
            plugin.getConstructor(Consumer::class.java, loaderType)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Unable to find plugin bootstrap constructor $pathToBootstrap", e)
        }

        val applyScafall : Consumer<Scafall> = Consumer {
            ScafallProvider.register(it)
        }

        return try {
            constructor.newInstance(applyScafall, loader)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Could not create plugin bootstrap instance", e)
        }
    }

}