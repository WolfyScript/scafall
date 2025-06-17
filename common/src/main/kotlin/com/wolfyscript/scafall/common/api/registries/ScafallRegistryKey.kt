package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.RegistryHolder
import com.wolfyscript.scafall.registry.RegistryKey
import com.wolfyscript.scafall.registry.RegistryReference
import com.wolfyscript.scafall.registry.ValueKey

class ScafallRegistryKey<R>(
    override val root: Key,
    override val registry: Key,
) : RegistryKey<R> {

    override fun reference(defaultHolder: () -> RegistryHolder): RegistryReference<R> {
        return ScafallRegistryReference(this, defaultHolder)
    }

    override fun <T : R> referenced(valueKey: Key, valueType: Class<T>): ValueKey<R, T> {
        return ScafallValueKey(this, valueKey, valueType)
    }

}