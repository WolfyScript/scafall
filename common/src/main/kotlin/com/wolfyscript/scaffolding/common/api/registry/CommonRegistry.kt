package com.wolfyscript.scaffolding.common.api.registry

import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.registry.Registry

class CommonRegistry<V> : Registry<V> {



    override fun get(key: Key): V? {
        TODO("Not yet implemented")
    }

    override fun keys(): Set<Key> {
        TODO("Not yet implemented")
    }

    override fun values(): Collection<V> {
        TODO("Not yet implemented")
    }

    override fun register(key: Key, value: V) {
        TODO("Not yet implemented")
    }

    override fun getKey(value: V): Key? {
        TODO("Not yet implemented")
    }

    override fun key(): Key {
        TODO("Not yet implemented")
    }


}