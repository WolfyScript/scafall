package com.wolfyscript.scaffolding.dependency

/**
 * Represents a Dependency of a type/instance.
 * This interface may be implemented and then provided via a custom [DependencyResolver] implementation.
 */
interface Dependency {
    /**
     * Checks if the dependency is loaded and available.
     *
     * @return true when available; false otherwise
     */
    val isAvailable: Boolean
}
