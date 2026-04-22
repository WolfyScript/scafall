package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ValueKey
import com.wolfyscript.scafall.registry.RegistryKey

interface RegistryFactory {

    fun <R, T: R> createRegistryKey(registry: RegistryKey<R>, key: Key, valueType: Class<T>) : ValueKey<R, T>

    fun <T> createRegistryType(root: Key, registryKey: Key) : RegistryKey<T>

}

inline fun <R, reified T: R> RegistryFactory.createRegistryKey(registry: RegistryKey<R>, key: Key): ValueKey<R, T> {
    return createRegistryKey(registry, key, T::class.java)
}