package com.wolfyscript.scafall.eval.operator

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.wolfyscript.scafall.config.jackson.RegistryKeyTypeIdResolver
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.parse
import com.wolfyscript.scafall.identifier.Keyed
import com.wolfyscript.scafall.identifier.StaticNamespacedKey.KeyBuilder.createKeyString

@JsonTypeIdResolver(
    RegistryKeyTypeIdResolver::class
)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "key")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder(value = ["key"])
abstract class Operator : Keyed {
    protected val key: Key

    constructor(namespacedKey: Key) {
        this.key = namespacedKey
    }

    constructor() {
        this.key = parse(createKeyString(javaClass))
    }

    @JsonIgnore
    override fun key(): Key {
        return key
    }
}
