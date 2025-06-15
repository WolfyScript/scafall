package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.parse
import com.wolfyscript.scafall.identifier.StaticNamespacedKey.KeyBuilder.createKeyString

abstract class AbstractValueProvider<V> : ValueProvider<V> {
    @JsonIgnore
    protected val key: Key

    protected constructor(key: Key) {
        this.key = key
    }

    protected constructor() {
        this.key = parse(createKeyString(javaClass))
    }

}
