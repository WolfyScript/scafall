package com.wolfyscript.scafall.registry

import com.google.common.base.Preconditions
import com.wolfyscript.scafall.identifier.Key
import java.util.*
import java.util.function.Supplier

abstract class AbstractRegistry<M : MutableMap<Key, V>, V>(
    override val key: Key,
    protected val map: M
) : Registry<V> {

    constructor(namespacedKey: Key, mapSupplier: Supplier<M>) : this(
        namespacedKey,
        mapSupplier.get()
    )

    override fun get(key: Key): V? {
        return map[key]
    }

    override fun register(key: Key, value: V) {
        Preconditions.checkState(!map.containsKey(key), "namespaced key '%s' already has an associated value!", key)
        map.put(key, value)
    }

    override fun iterator(): Iterator<V> {
        return map.values.iterator()
    }

    override fun keySet(): Set<Key> {
        return Collections.unmodifiableSet(map.keys)
    }

    override fun values(): Collection<V> {
        return Collections.unmodifiableCollection(map.values)
    }

    override fun entrySet(): Set<Map.Entry<Key, V>> {
        return Collections.unmodifiableSet(map.entries)
    }
}
