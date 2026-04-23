package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.registry.ScafallRegistryKey
import com.wolfyscript.scafall.registry.ScafallValueKey
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ValueKey
import com.wolfyscript.scafall.registry.RegistryKey

internal class CommonRegistryFactory : RegistryFactory {

    override fun <R, T : R> createRegistryKey(registry: RegistryKey<R>, key: Key, valueType: Class<T>): ValueKey<R, T> {
        return ScafallValueKey(registry, key, valueType)
    }

    override fun <T> createRegistryType(
        root: Key,
        registryKey: Key,
    ): RegistryKey<T> {
        return ScafallRegistryKey(root, registryKey)
    }

}