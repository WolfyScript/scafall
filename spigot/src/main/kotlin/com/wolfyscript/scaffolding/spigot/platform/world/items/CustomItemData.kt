package com.wolfyscript.scaffolding.spigot.platform.world.items

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import com.wolfyscript.scaffolding.Copyable
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeIdResolver
import com.wolfyscript.scaffolding.config.jackson.KeyedTypeResolver
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Keyed

@JsonTypeResolver(KeyedTypeResolver::class)
@JsonTypeIdResolver(
    KeyedTypeIdResolver::class
)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "id")
@JsonPropertyOrder(value = ["id"])
abstract class CustomItemData protected constructor(@field:JsonProperty("id") private val id: Key) : Keyed,
    Copyable<CustomItemData> {
    @JsonIgnore
    override fun key(): Key {
        return id
    }
}
