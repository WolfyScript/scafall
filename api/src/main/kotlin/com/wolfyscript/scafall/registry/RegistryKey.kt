package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key

interface RegistryKey<R> {

    companion object {

        fun <T> of(root: Key, registryKey: Key): RegistryKey<T> {
            return ScafallProvider.get().factories.registryFactory.createRegistryType(root, registryKey)
        }

        fun <T> of(registryKey: Key): RegistryKey<T> {
            return of(ScafallRegistryTypes.root, registryKey)
        }

    }

    val root: Key

    val registry: Key

    fun reference(): RegistryReference<R> {
        return reference { ScafallProvider.get().registries }
    }

    fun reference(defaultHolder: () -> RegistryHolder): RegistryReference<R>

    fun <T : R> referenced(valueKey: Key, valueType: Class<T>): ValueKey<R, T>
}

inline fun <R, reified T : R> RegistryKey<R>.referenced(valueKey: Key): ValueKey<R, T> {
    return referenced(valueKey, T::class.java)
}
