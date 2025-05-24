package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key

/**
 * Provides safe access to Registry values.
 */
interface RegistryKey<T> {

    /**
     * The registry to fetch the value from.
     */
    val registry: Registry<T>

    /**
     * The key of the value in the [registry]
     */
    val key: Key

    /**
     * Tries to get the value from the Registry this Key is bound to.
     *
     * @return A Result of the value when available; or a failed Result with the Error that occurred.
     */
    fun get(): Result<T>

    companion object {

        inline fun <reified T> of(registry: Registry<T>, key: Key): RegistryKey<T> {
            return ScafallProvider.get().factories.registryFactory.createRegistryKey(registry, key)
        }

    }

}