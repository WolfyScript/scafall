package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.RegistryHolder
import com.wolfyscript.scafall.registry.ValueKey
import com.wolfyscript.scafall.registry.RegistryKey
import com.wolfyscript.scafall.registry.ValueReference

class ScafallValueKey<R, T: R>(
    override val registry: RegistryKey<R>,
    override val key: Key,
    override val valueType: Class<T>
) : ValueKey<R, T> {

    override fun reference(defaultHolder: () -> RegistryHolder): ValueReference<R, T> {
        return ScafallValueReference(this, defaultHolder)
    }

}