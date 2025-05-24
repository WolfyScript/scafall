package com.wolfyscript.scafall.common.api.factories

import com.wolfyscript.scafall.common.api.registries.CommonRegistryKey
import com.wolfyscript.scafall.factories.RegistryFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.Registry
import com.wolfyscript.scafall.registry.RegistryKey

class CommonRegistryFactory : RegistryFactory {

    override fun <T> createRegistryKey(
        registry: Registry<T>,
        key: Key,
    ): RegistryKey<T> {
        return CommonRegistryKey(registry, key)
    }

}