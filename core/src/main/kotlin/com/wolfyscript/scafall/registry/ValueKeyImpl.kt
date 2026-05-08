package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.identifier.Key

internal class ValueKeyImpl<R, T: R>(
    override val registry: RegistryKey<R>,
    override val key: Key,
    override val valueType: Class<T>
) : ValueKey<R, T> {

    override fun reference(defaultHolder: () -> RegistryHolder): ValueReference<R, T> {
        return ValueReferenceImpl(this, defaultHolder)
    }

}