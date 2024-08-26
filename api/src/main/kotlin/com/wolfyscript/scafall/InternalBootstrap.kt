package com.wolfyscript.scafall

import com.wolfyscript.scafall.loader.PluginBootstrap
import com.wolfyscript.scafall.loader.ScaffoldingBootstrap
import com.wolfyscript.scafall.loader.ScaffoldingModule
import org.jetbrains.annotations.ApiStatus.Internal
import java.lang.reflect.Constructor
import java.util.function.Consumer

/**
 * Creates internal bootstrap classes that are linked to this API module.
 *
 */
@Internal
internal class InternalBootstrap : ScaffoldingBootstrap {

    override fun createScaffoldingModule(entrypoint: String, loader: Any): ScaffoldingModule {
        val moduleClass: Class<out ScaffoldingModule> = try {
            javaClass.classLoader.loadClass(entrypoint).asSubclass(ScaffoldingModule::class.java)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException("Unable to load module: $entrypoint", e)
        }

        val constructor: Constructor<out ScaffoldingModule> = try {
            moduleClass.getConstructor(loader.javaClass, Scafall::class.java)
        } catch (e: ReflectiveOperationException) {
            throw RuntimeException("Unable to find constructor for module: $entrypoint", e)
        }

        val module: ScaffoldingModule = try {
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