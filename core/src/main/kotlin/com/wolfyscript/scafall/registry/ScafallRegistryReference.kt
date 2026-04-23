package com.wolfyscript.scafall.registry

class ScafallRegistryReference<R>(override val key: RegistryKey<R>, val defaultHolder: () -> RegistryHolder) : RegistryReference<R> {

    override fun resolve(): Result<Registry<R>> {
        return resolve(defaultHolder())
    }

    override fun resolve(holder: RegistryHolder): Result<Registry<R>> {
        val result = holder.get(key)
        if (result.isFailure) {
            return Result.failure(IllegalStateException("No registry found for key $key and holder $holder", result.exceptionOrNull()))
        }
        return Result.success(result.getOrThrow())
    }

    override fun resolveOrThrow(): Registry<R> {
        return resolveOrThrow(defaultHolder())
    }

    override fun resolveOrThrow(holder: RegistryHolder): Registry<R> {
        return holder.get(key).getOrThrow()
    }
}