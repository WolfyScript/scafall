package com.wolfyscript.scafall.loader.module

import java.lang.reflect.Constructor

interface ImplementationModuleBootstrap<T> {

    /**
     * Called by the implementation on the specific platform.
     *
     * [loader] is the instance of the implementation loader. Usually the plugin loaded by the platform.
     */
    fun loadImplementationModule(bridgeType: Class<T>, pathToModule: String, loaderType: Class<*>, loader: Any): Module<T> {
        if (bridgeApplied()) {
            throw IllegalStateException("Bridge $bridgeType is already initialized!")
        }

        val plugin: Class<out Module<T>> = try {
            javaClass.classLoader.loadClass(pathToModule).asSubclass(Module::class.java) as Class<out Module<T>>
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Unable to load implementation module class $pathToModule", e)
        }

        val constructor: Constructor<out Module<T>> = try {
            plugin.getConstructor(loaderType)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Unable to find implementation module constructor $pathToModule", e)
        }
        val module = try {
            constructor.newInstance(loader)
        } catch (e: ReflectiveOperationException) {
            throw IllegalStateException("Could not create plugin bootstrap instance", e)
        }
        applyBridge(module)
        return module
    }

    fun bridgeApplied(): Boolean

    /**
     * Called by the implementation to apply the created bridge.
     */
    fun applyBridge(module: Module<T>)
}
