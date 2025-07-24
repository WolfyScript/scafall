package com.wolfyscript.scafall.loader.module

import com.wolfyscript.scafall.loader.InnerJarClassloader
import java.lang.reflect.Constructor

/**
 * Util class to load a [Module] from an inner-jar and making it available to the current module.
 *
 * For example, loading it from the inner-jar and registering the bridge as a Singleton.
 */
abstract class StandaloneInternalBootstrap<T>(val moduleBaseType: Class<out Module<T>>, val innerJarClassloader: ClassLoader) {

    /**
     * Loads the module from the inner-jar for further processing within the shared module.
     *
     * [pathToModule] - Path to the module implementation inside the inner-jar
     *
     * [loader] - The object that the module was loaded from. E.g. the boostrap JavaPlugin on Spigot
     *
     * [loaderType] - The type of that loader. E.g. for spigot JavaPlugin
     */
    fun loadModuleFromInnerJar(pathToModule: String, loaderType: Class<*>, loader: Any): Module<T> {
        if (registered) {
            throw IllegalStateException("Bootstrap $pathToModule is already initialized!")
        }

        val plugin: Class<out Module<T>> = try {
            innerJarClassloader.loadClass(pathToModule).asSubclass(moduleBaseType)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Unable to load module class $pathToModule", e)
        }

        val constructor: Constructor<out Module<T>> = try {
            plugin.getConstructor(ClassLoader::class.java, loaderType)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Unable to find module constructor $pathToModule", e)
        }

        val module = try {
            constructor.newInstance(innerJarClassloader, loader)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Could not create plugin bootstrap instance", e)
        }
        register(module)
        module.onInit()
        return module
    }

    /**
     * Keeps track if the module was already registered.
     */
    abstract val registered: Boolean

    /**
     * Called when the module was successfully loaded and can be registered.
     */
    protected abstract fun register(module: Module<T>)

}