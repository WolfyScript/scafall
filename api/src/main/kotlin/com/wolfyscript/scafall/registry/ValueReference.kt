package com.wolfyscript.scafall.registry

interface ValueReference<R, T: R> {

    val key: ValueKey<R, T>

    fun resolve(): Result<T>

    fun resolve(holder: RegistryHolder): Result<T>

    fun resolveOrThrow(): T

    fun resolveOrThrow(holder: RegistryHolder): T

}