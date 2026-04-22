package com.wolfyscript.scafall.registry

interface RegistryReference<R> {

    val key: RegistryKey<R>

    fun resolve(): Result<Registry<R>>

    fun resolve(holder: RegistryHolder): Result<Registry<R>>

    fun resolveOrThrow(): Registry<R>

    fun resolveOrThrow(holder: RegistryHolder): Registry<R>

}