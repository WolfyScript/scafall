package com.wolfyscript.scafall.registry

interface RegistryHolder {

    fun <T> get(type: RegistryKey<T>): Result<Registry<T>>

}