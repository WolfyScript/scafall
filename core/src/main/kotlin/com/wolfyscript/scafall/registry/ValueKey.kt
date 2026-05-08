package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key

/**
 * A key for a value in a registry.
 * Allows for accessing values in a Registry in a type-safe manner.
 * It ensures that the value retrieved is of the expected type, preventing runtime errors due to type mismatches.
 *
 * @param R The type of the Registry.
 * @param T The type of the value that this key is associated with.
 */
interface ValueKey<R, T: R> {

    /**
     * The stored type class of the expected value.
     */
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
     * Creates a reference that can be resolved against the default or custom [RegistryHolder].
     *
     * This uses the scafall [RegistryHolder] as the default registry holder for the [ValueReference].
     *
     * @return A [ValueReference] that can be used to fetch the value associated with this key.
     */
    fun reference(): ValueReference<R, T> {
        return reference { ScafallProvider.get().registries }
    }

    /**
     * Creates a reference that can be resolved against the default or custom [RegistryHolder]
     *
     * @param defaultHolder The default registry holder
     */
    fun reference(defaultHolder: () -> RegistryHolder): ValueReference<R, T>

    companion object {

        inline fun <reified R, reified T: R> of(registry: RegistryKey<R>, key: Key): ValueKey<R, T> {
            return ValueKeyImpl(registry, key, T::class.java)
        }

    }

}