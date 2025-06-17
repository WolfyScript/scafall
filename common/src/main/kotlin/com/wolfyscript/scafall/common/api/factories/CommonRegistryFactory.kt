package com.wolfyscript.scafall.common.api.factories

import com.wolfyscript.scafall.common.api.registries.ScafallValueKey
import com.wolfyscript.scafall.factories.RegistryFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ValueKey
import com.wolfyscript.scafall.registry.RegistryKey

class CommonRegistryFactory : RegistryFactory {

    override fun <T> createRegistryKey(
        registry: RegistryKey<T>,
        key: Key,
    ): ValueKey<T> {
        return ScafallValueKey(registry, key)
    }

    override fun <T> createRegistryType(
        root: Key,
        registryKey: Key,
    ): RegistryKey<T> {
        TODO("Not yet implemented")
    }

}