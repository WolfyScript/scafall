package com.wolfyscript.scafall.registry

/**
 * An interface representing an object that can hold registries.
 */
interface RegistryHolder {

    /**
     * Retrieves a registry based on the provided [RegistryKey].
     *
     * @param type The [RegistryKey] specifying the registry to retrieve.
     * @return A [Result] containing the requested [Registry] or an exception if retrieval fails.
     */
    fun <T> get(type: RegistryKey<T>): Result<Registry<T>>

}