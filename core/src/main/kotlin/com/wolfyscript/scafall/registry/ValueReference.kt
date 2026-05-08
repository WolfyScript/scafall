package com.wolfyscript.scafall.registry

/**
 * A reference to a value in a registry.
 *
 * @param R The type of the registry.
 * @param T The type of the value, which is a subtype of R.
 */
interface ValueReference<R, T: R> {

    /**
     * The key associated with this value reference.
     */
    val key: ValueKey<R, T>

    /**
     * Resolve the value reference using the default [RegistryHolder].
     *
     * @return A result containing the resolved value or an error.
     */
    fun resolve(): Result<T>

    /**
     * Resolve the value reference against a specific [RegistryHolder].
     *
     * @param holder The registry holder to resolve against.
     * @return A result containing the resolved value or an error.
     */
    fun resolve(holder: RegistryHolder): Result<T>

    /**
     * Resolve the value reference against the default [RegistryHolder] or throw an error if it cannot be resolved.
     *
     * @return The resolved value.
     */
    fun resolveOrThrow(): T

    /**
     * Resolve the value reference against a specific [RegistryHolder] or throw an error if it cannot be resolved, with additional context.
     *
     * @param holder The registry holder to resolve against.
     * @return The resolved value.
     */
    fun resolveOrThrow(holder: RegistryHolder): T

}