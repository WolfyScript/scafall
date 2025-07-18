package com.wolfyscript.scafall.compat

import com.wolfyscript.scafall.identifier.Key

/**
 * Keeps track of Dependencies.
 *
 * Dependencies may be in different states depending on how they are loaded.
 *
 * - `Loaded` - The mod/plugin is available and present in the classpath. Data may not be loaded nor registered.
 * - `Initialized` - The data of the mod/plugin has been loaded, registered, and can be used.
 */
interface DependencyManager {

    /**
     * Marks a Dependency as loaded and loads it into the manager.
     * This means after this, [isLoaded] will be valid for the specified dependency.
     */
    fun loadDependency(id: Key, dependency: Dependency)

    /**
     * Notifies the Manager that the specified Dependency has been initialized.
     */
    fun dependencyInitiated(id: Key): Boolean

    fun getDependency(id: Key): Dependency?

    /**
     * Checks if a dependency is loaded
     */
    fun isLoaded(id: Key): Boolean

    /**
     * Runs the given [action] only if the [dependency] has been loaded.
     */
    fun <R> runIfLoaded(dependency: Key, action: () -> R): R?

    /**
     * Runs the given [action] only if the [dependency] has been initialized.
     */
    fun <R> runIfInitialized(dependency: Key, action: () -> R): R?

    /**
     * Registers a Callback that will get called when the dependency has been initialized, so all data has been loaded and registered.
     */
    fun onDependencyInitialized(dependency: Key, fn: (Dependency) -> Unit)

}
