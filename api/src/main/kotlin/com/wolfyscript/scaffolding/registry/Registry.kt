package com.wolfyscript.scaffolding.registry

import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Keyed

interface Registry<V> : Keyed {

    fun get(key: Key): V?

    fun getKey(value: V): Key?

    fun register(key: Key, value: V)

    fun keys(): Set<Key>

    fun values(): Collection<V>

}