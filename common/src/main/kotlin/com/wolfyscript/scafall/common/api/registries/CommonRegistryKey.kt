package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.Registry
import com.wolfyscript.scafall.registry.RegistryKey

class CommonRegistryKey<T>(
    override val registry: Registry<T>,
    override val key: Key
) : RegistryKey<T> {

    override fun get(): Result<T> {
        val result = registry[key]
        if (result == null) {
            return Result.failure(IllegalArgumentException("Cannot find entry for key $key in registry $registry"))
        }
        return Result.success(result)
    }

}