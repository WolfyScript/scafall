package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key

interface RegistryKey<R> {

    companion object {

        fun <T> of(root: Key, registryKey: Key): RegistryKey<T> {
            return ScafallProvider.get().factories.registryFactory.createRegistryType(root, registryKey)
        }

        fun <T> of(registryKey: Key): RegistryKey<T> {
            return of(ScafallRegistryTypes.root, registryKey)
        }

    }

    val root: Key

    val registry: Key

    fun reference(): RegistryReference<R>

}
