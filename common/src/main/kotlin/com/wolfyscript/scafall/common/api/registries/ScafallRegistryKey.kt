package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.RegistryKey
import com.wolfyscript.scafall.registry.RegistryReference
import com.wolfyscript.scafall.registry.ValueReference

class ScafallRegistryKey<R>(
    override val root: Key,
    override val registry: Key,
) : RegistryKey<R> {

    override fun reference(): RegistryReference<R> {
        return ScafallRegistryReference(this) { ScafallProvider.get().registries }
    }

}