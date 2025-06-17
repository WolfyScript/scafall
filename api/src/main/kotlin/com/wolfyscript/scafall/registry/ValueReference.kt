package com.wolfyscript.scafall.registry

interface ValueReference<T> {

    val key: ValueKey<T>

    fun resolve(): Result<T>

    fun resolve(holder: RegistryHolder): Result<T>

    fun resolveOrThrow(): T

    fun resolveOrThrow(holder: RegistryHolder): T

}