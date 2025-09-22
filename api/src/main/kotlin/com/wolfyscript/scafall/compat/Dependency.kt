package com.wolfyscript.scafall.compat

/**
 * Represents a Dependency of a type/instance.
 *
 * A dependency may be in two states
 * - `loaded` - The plugin or the mod is loaded
 * - `initialized` - All the available data the dependency provides is initialized and available for use.
 *
 * ### Implementing a Dependency
 * When loaded a new instant of the dependency object is created. At this point (constructor, init) the plugin/mod may not be loaded/enabled.
 * You may only assume that it is available in the classpath!
 * This is usually when events, that listen to the mods/plugins lifecycle to initialize it later, are registered.
 *
 * A dependency may use plugin/mod specific APIs to wait for the mod/plugin and its data to be available.
 * Once available, the dependency must be initiated via [DependencyManager.initiateDependency]
 * to properly call the [onInit] and then notify all the listeners that may wait for this dependency.
 *
 * The [onInit] should mark the dependency as [isInitialized] if it was successfully initialized.
 * Otherwise, it is not initialized, a warning is printed and listeners are not notified.
 */
interface Dependency {

    /**
     * Determines if this dependency has been initialized.
     * Should be set within the [onInit].
     */
    val isInitialized: Boolean

    /**
     * Called when a dependency is initialized.
     * At this point the dependency is available and data is loaded.
     *
     * Should set [isInitialized] according if the initialization was successful.
     * If [isInitialized] is false at the end of this function then a warning is printed and listeners are not notified.
     *
     * This function may throw errors, in which case the error is printed and listeners are not notified.
     */
    fun onInit() {}

}
