package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.ValueKey
import com.wolfyscript.scafall.registry.RegistryKey
import com.wolfyscript.scafall.registry.ValueReference

class ScafallValueKey<T>(
    override val registry: RegistryKey<T>,
    override val key: Key
) : ValueKey<T> {

    override fun reference(): ValueReference<T> {
        return ScafallValueReference(this) { ScafallProvider.get().registries }
    }


}