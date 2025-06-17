package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ValueKey
import com.wolfyscript.scafall.registry.RegistryKey

interface RegistryFactory {

    fun <T> createRegistryKey(registry: RegistryKey<T>, key: Key) : ValueKey<T>

    fun <T> createRegistryType(root: Key, registryKey: Key) : RegistryKey<T>

}