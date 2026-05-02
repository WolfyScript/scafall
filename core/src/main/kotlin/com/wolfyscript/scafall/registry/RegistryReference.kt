package com.wolfyscript.scafall.registry

/**
 * Represents a reference to a registry that can be resolved to an actual [Registry].
 *
 * Provides methods to resolve the registry against either the default or through a provided
 * [RegistryHolder].
 *
 * @param [R] The type of the value stored within the registry.
 */
interface RegistryReference<R> {

    val key: RegistryKey<R>

    /**
     * Attempts to resolve the registry reference.
     *
     * @return A [Result] containing either the resolved [Registry] or a failure.
     */
    fun resolve(): Result<Registry<R>>

    /**
     * Attempts to resolve the registry reference against a provided [RegistryHolder].
     *
     * @param holder The [RegistryHolder] used to perform the resolution.
     * @return A [Result] containing either the resolved [Registry] or a failure.
     */
    fun resolve(holder: RegistryHolder): Result<Registry<R>>

    /**
     * Attempts to resolve the registry reference against the scafall [RegistryHolder] and returns the resolved [Registry].
     * If resolution fails, an exception is thrown.
     *
     * @return The resolved [Registry].
     * @throws Exception if the resolution process encounters an error or the registry cannot be found in the provided [RegistryHolder].
     */
    fun resolveOrThrow(): Registry<R>

    /**
     * Attempts to resolve the registry reference against a provided [RegistryHolder] and returns the resolved [Registry].
     * If resolution fails, an exception is thrown.
     *
     * @param holder The [RegistryHolder] used to perform the resolution.
     * @return The resolved [Registry].
     * @throws Exception if the resolution process encounters an error or the registry cannot be found in the provided [RegistryHolder].
     */
    fun resolveOrThrow(holder: RegistryHolder): Registry<R>

}