package com.wolfyscript.scafall.registry

import com.wolfyscript.scafall.identifier.Key

/**
 * A simple registry, used for basic use cases.
 *
 * @param <V> The type of the value.
</V> */
open class RegistrySimple<V> : AbstractRegistry<MutableMap<Key, V>, V> {

    constructor(namespacedKey: Key) : super(
        namespacedKey,
        HashMap<Key, V>()
    )

    constructor(namespacedKey: Key, type: Class<V>) : super(
        namespacedKey,
        HashMap<Key, V>(),
        type
    )
}
