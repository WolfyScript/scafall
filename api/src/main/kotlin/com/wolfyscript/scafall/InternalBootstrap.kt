package com.wolfyscript.scafall

import com.wolfyscript.scafall.loader.InnerJarClassloader
import com.wolfyscript.scafall.loader.PluginBootstrap
import com.wolfyscript.scafall.loader.ScafallBootstrap
import org.jetbrains.annotations.ApiStatus.Internal
import java.lang.reflect.Constructor
import java.util.function.Consumer

/**
 * Creates internal bootstrap classes that are linked to this API module.
 *
 */
@Internal
internal class InternalBootstrap(val classLoader: InnerJarClassloader) : ScafallBootstrap {

    override fun initScaffoldingPlatform(pathToBootstrap: String, loaderType: Class<*>, loader: Any): PluginBootstrap {
        if (ScafallProvider.registered()) {
            throw IllegalStateException("Scaffolding Platform is already initialized!")
        }

        val plugin: Class<out PluginBootstrap> = try {
            classLoader.loadClass(pathToBootstrap).asSubclass(PluginBootstrap::class.java)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Unable to load plugin bootstrap class $pathToBootstrap", e)
        }

        val constructor: Constructor<out PluginBootstrap> = try {
            plugin.getConstructor(Consumer::class.java, ClassLoader::class.java, loaderType)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Unable to find plugin bootstrap constructor $pathToBootstrap", e)
        }

        val applyScafall : Consumer<Scafall> = Consumer {
            ScafallProvider.register(it)
        }

        return try {
            constructor.newInstance(applyScafall, classLoader, loader)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Could not create plugin bootstrap instance", e)
        }
    }

}