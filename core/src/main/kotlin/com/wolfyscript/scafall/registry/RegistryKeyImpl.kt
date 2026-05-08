package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.identifier.Key

internal class RegistryKeyImpl<R>(
    override val root: Key,
    override val registry: Key,
) : RegistryKey<R> {

    override fun reference(defaultHolder: () -> RegistryHolder): RegistryReference<R> {
        return RegistryReferenceImpl(this, defaultHolder)
    }

    override fun <T : R> referenced(valueKey: Key, valueType: Class<T>): ValueKey<R, T> {
        return ValueKeyImpl(this, valueKey, valueType)
    }

}