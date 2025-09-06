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
     * Notifies the Manager that the specified Dependency has failed to initialize.
     * This means after this, [isLoaded] will be invalid for the specified dependency.
     * The Manager will not attempt to load the dependency again.
     */
    fun failedToInitDependency(id: Key)

    /**
     * Notifies the Manager that the specified Dependency has been initialized.
     */
    fun initiateDependency(id: Key): Boolean

    /**
     * Gets a dependency by its [id] if it has been loaded.
     *
     * @return The dependency or null if it has not been loaded.
     */
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
    fun onDependencyInitialized(dependency: Key? = null, fn: (Dependency) -> Unit)

    /**
     * Registers a Callback that will get called when all dependencies have been initialized.
     * Called immediately if all dependencies are already initialized.
     */
    fun onAllDependenciesInitialized(action: (Map<Key, Dependency>) -> Unit)

    /**
     * Registers a Callback that will get called when the dependency has failed to initialize.
     */
    fun onDependencyFailed(dependency: Key? = null, fn: (Dependency) -> Unit)
}
