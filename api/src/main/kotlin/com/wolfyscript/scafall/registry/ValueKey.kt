package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key

/**
 * Provides safe access to Registry values.
 */
interface ValueKey<T> {

    /**
     * The registry to fetch the value from.
     */
    val registry: RegistryKey<T>

    /**
     * The key of the value in the [registry]
     */
    val key: Key

    /**
     * Creates a reference that can be resolved against the default or custom [RegistryHolder]
     */
    fun reference(): ValueReference<T>

    companion object {

        inline fun <reified T> of(registry: RegistryKey<T>, key: Key): ValueKey<T> {
            return ScafallProvider.get().factories.registryFactory.createRegistryKey(registry, key)
        }

    }

}