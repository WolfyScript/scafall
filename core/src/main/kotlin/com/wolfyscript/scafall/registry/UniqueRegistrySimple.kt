package com.wolfyscript.scafall.registry

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.wolfyscript.scafall.identifier.Key

/**
 * A simple registry, used for basic use cases.
 *
 * @param <V> The type of the value.
</V> */
open class UniqueRegistrySimple<V> : AbstractRegistry<BiMap<Key, V>, V> {
    constructor(namespacedKey: Key) : super(
        namespacedKey,
        HashBiMap.create<Key, V>()
    )

    constructor(namespacedKey: Key, type: Class<V>) : super(
        namespacedKey,
        HashBiMap.create<Key, V>()
    )

}
