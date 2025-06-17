package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.registry.RegistryHolder
import com.wolfyscript.scafall.registry.ValueKey
import com.wolfyscript.scafall.registry.ValueReference

class ScafallValueReference<T>(
    override val key: ValueKey<T>,
    private val defaultHolder: () -> RegistryHolder,
) : ValueReference<T> {

    override fun resolve(): Result<T> {
        return resolve(defaultHolder())
    }

    override fun resolve(holder: RegistryHolder): Result<T> {
        val result = holder.get(key.registry)
        if (result.isFailure) {
            return Result.failure(IllegalStateException("No registry found for key $key and holder $holder", result.exceptionOrNull()))
        }
        val registry = result.getOrThrow()
        return registry[key.key]?.let { Result.success(it) } ?: Result.failure(IllegalStateException("No value found for key $key in registry $registry"))
    }

    override fun resolveOrThrow(): T {
        return resolveOrThrow(defaultHolder())
    }

    override fun resolveOrThrow(holder: RegistryHolder): T {
        val registry = holder.get(key.registry).getOrThrow()
        return registry[key.key] ?: throw IllegalStateException("No value found for key $key in registry $registry")
    }
}