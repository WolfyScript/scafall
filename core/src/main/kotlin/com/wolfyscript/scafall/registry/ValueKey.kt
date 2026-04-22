package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key

/**
 * Provides safe access to Registry values.
 */
interface ValueKey<R, T: R> {

    val valueType: Class<T>

    /**
     * The registry to fetch the value from.
     */
    val registry: RegistryKey<R>

    /**
     * The key of the value in the [registry]
     */
    val key: Key

    /**
     * Creates a reference that can be resolved against the default or custom [RegistryHolder]
     */
    fun reference(): ValueReference<R, T> {
        return reference { ScafallProvider.get().registries }
    }

    fun reference(defaultHolder: () -> RegistryHolder): ValueReference<R, T>

    companion object {

        inline fun <reified R, reified T: R> of(registry: RegistryKey<R>, key: Key): ValueKey<R, T> {
            return ScafallProvider.get().factories.registryFactory.createRegistryKey(registry, key, T::class.java)
        }

    }

}