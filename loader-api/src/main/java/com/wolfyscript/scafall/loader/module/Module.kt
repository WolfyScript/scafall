package com.wolfyscript.scafall.loader.module

/**
 * The module is implemented in the platform specific implementation
 * and handles the instantiation of the Bridge, that links the implementation to the API.
 *
 * [T] The type of the Bridge interface that is implemented in the platform specific implementation module.
 */
interface Module<T> {

    /**
     * Called when the module was initiated and/or registered.
     *
     * Not called when the Module loaded manually using the loader-api, then this needs to be run manually too.
     */
    fun onInit() {}

    /**
     * Called when the module was created and is now being initiated by the platform.
     */
    fun onLoad()

    /**
     * Called when all initiations are completed and the Module is ready to be used.
     */
    fun onEnable()

    /**
     * Called when the platform is unloading. This is unloaded before scafall.
     */
    fun onUnload()

    /**
     * The instance that links the bridge interface to this platform module
     */
    val bridge: T
}
