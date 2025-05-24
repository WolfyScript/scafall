package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.Registry
import com.wolfyscript.scafall.registry.RegistryKey

interface RegistryFactory {

    fun <T> createRegistryKey(registry: Registry<T>, key: Key) : RegistryKey<T>

}