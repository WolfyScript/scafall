package com.wolfyscript.scafall.common.api.registries

import com.wolfyscript.scafall.registry.RegistryHolder
import com.wolfyscript.scafall.registry.ValueKey
import com.wolfyscript.scafall.registry.ValueReference

class ScafallValueReference<R, T: R>(
    override val key: ValueKey<R, T>,
    private val defaultHolder: () -> RegistryHolder,
) : ValueReference<R, T> {

    override fun resolve(): Result<T> {
        return resolve(defaultHolder())
    }

    override fun resolve(holder: RegistryHolder): Result<T> {
        val result = holder.get(key.registry)
        if (result.isFailure) {
            return Result.failure(IllegalStateException("No registry found for key $key and holder $holder", result.exceptionOrNull()))
        }
        val registry = result.getOrThrow()
        val value = registry[key.key] ?: return Result.failure<T>(IllegalStateException("No value found for key $key in registry $registry"))
        if (!key.valueType.isInstance(value)) {
            return Result.failure(IllegalStateException("Found value for $key, but of different type. Expected ${key.valueType}, got ${value::class.java}!"))
        }
        return Result.success(key.valueType.cast(value))
    }

    override fun resolveOrThrow(): T {
        return resolveOrThrow(defaultHolder())
    }

    override fun resolveOrThrow(holder: RegistryHolder): T {
        val registry = holder.get(key.registry).getOrThrow()
        val value = registry[key.key] ?: throw IllegalStateException("No value found for key $key in registry $registry")
        if (!key.valueType.isInstance(value)) {
            throw IllegalStateException("Found value for $key, but of different type. Expected ${key.valueType}, got ${value::class.java}!")
        }
        return key.valueType.cast(value)
    }
}