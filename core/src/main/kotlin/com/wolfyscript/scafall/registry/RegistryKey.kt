package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key

/**
 * An identifier for a registry within a root registry.
 *
 * @param R the value type the registry holds
 */
interface RegistryKey<R> {

    companion object {

        /**
         * Creates a new [RegistryKey] instance for a new registry in the specified root registry.
         *
         * @param root The root registry that holds the new registry.
         * @param registryIdentifier The [Key] that identifies the new registry within the root registry.
         * @return A new [RegistryKey] holding values of type [T].
         */
        fun <T> of(root: Key, registryIdentifier: Key): RegistryKey<T> {
            return RegistryKeyImpl(root, registryIdentifier)
        }

        /**
         * Creates a new [RegistryKey] instance for a new registry in the scafall default root registry.
         *
         * @param registryIdentifier The [Key] that identifies the new registry within the root registry.
         * @return A new [RegistryKey] holding values of type [T].
         */
        fun <T> of(registryIdentifier: Key): RegistryKey<T> {
            return of(ScafallRegistryTypes.root, registryIdentifier)
        }

    }

    val root: Key

    val registry: Key

    /**
     * Creates a [RegistryReference] for this registry in the default [RegistryHolder].
     *
     * @return A [RegistryReference] pointing to the root registry.
     */
    fun reference(): RegistryReference<R> {
        return reference { ScafallProvider.get().registries }
    }

    /**
     * Creates a [RegistryReference] for this registry using a custom provider for the [RegistryHolder].
     *
     * @param defaultHolder A function that returns the [RegistryHolder] used to resolve the registry.
     * @return A new [RegistryReference] that resolves against the provided [RegistryHolder].
     */
    fun reference(defaultHolder: () -> RegistryHolder): RegistryReference<R>

    /**
     * Creates a [ValueKey] for a specific entry within this registry.
     *
     * @param valueKey The [Key] used to identify the value within the registry.
     * @param valueType The [Class] of the value type.
     * @return A [ValueKey] representing the specified value in this registry.
     */
    fun <T : R> referenced(valueKey: Key, valueType: Class<T>): ValueKey<R, T>
}

/**
 * Creates a [ValueKey] for a specific entry within this registry using a reified type.
 *
 * @param valueKey The [Key] used to identify the value within the registry.
 * @return A [ValueKey] representing the specified value in this registry.
 */
inline fun <R, reified T : R> RegistryKey<R>.referenced(valueKey: Key): ValueKey<R, T> {
    return referenced(valueKey, T::class.java)
}
