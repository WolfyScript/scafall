package com.wolfyscript.scafall.loader.module

import java.lang.reflect.Constructor

/**
 * Util class to load a [Module] from an inner-jar and making it available to the current module.
 *
 * For example, loading it from the inner-jar and registering the bridge as a Singleton.
 */
abstract class StandaloneInternalBootstrap<T: Module<*, *>>(val moduleBaseType: Class<out T>, val classloader: ClassLoader) {

    /**
     * Loads the module from the inner-jar for further processing within the shared module.
     *
     * [pathToModule] - Path to the module implementation inside the inner-jar
     *
     * [loader] - The object that the module was loaded from. E.g. the boostrap JavaPlugin on Spigot
     *
     * [loaderType] - The type of that loader. E.g. for spigot JavaPlugin
     */
    fun loadModule(pathToModule: String, loaderType: Class<*>, loader: Any): T {
        if (registered) {
            throw IllegalStateException("Bootstrap $moduleBaseType is already initialized!")
        }

        val plugin: Class<out T> = try {
            classloader.loadClass(pathToModule).asSubclass(moduleBaseType)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Unable to load module class $pathToModule", e)
        }

        val constructor: Constructor<out T> = try {
            plugin.getConstructor(ClassLoader::class.java, loaderType)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Unable to find module constructor $pathToModule", e)
        }

        val module = try {
            constructor.newInstance(classloader, loader)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Could not create plugin bootstrap instance", e)
        }
        register(module)
        module.onInit()
        onCompleted(module)
        return module
    }

    /**
     * A simple loading function
     */
    fun <C: T> loadModule(loader: () -> C) : C {
        if (registered) {
            throw IllegalStateException("Bootstrap $moduleBaseType is already initialized!")
        }
        val module = loader()
        register(module)
        module.onInit()
        onCompleted(module)
        return module
    }

    open fun loadInternal(): T {
        throw NotImplementedError("Not supported by this module")
    }

    /**
     * Keeps track if the module was already registered.
     */
    abstract val registered: Boolean

    /**
     * Called when the module was successfully loaded and can be registered.
     */
    protected abstract fun register(module: T)

    protected open fun onCompleted(module: T) {}

}