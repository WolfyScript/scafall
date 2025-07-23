package com.wolfyscript.scafall.compat

/**
 * Represents a Dependency of a type/instance.
 *
 * A dependency may be in two states
 * - `loaded` - The plugin is enabled, or the mod is loaded
 * - `initialized` - All the available data the dependency provides is initialized and available for use.
 */
interface Dependency {

    val isInitialized: Boolean

}
