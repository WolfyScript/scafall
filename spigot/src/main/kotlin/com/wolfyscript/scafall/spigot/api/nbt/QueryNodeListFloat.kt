package com.wolfyscript.scafall.spigot.api.nbt

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.nbtapi.NBTType

@StaticNamespacedKey(key = "list/float")
class QueryNodeListFloat : QueryNodeList<Float> {
    constructor(
        @JsonProperty("elements") elements: List<Element<Float>>,
        @JacksonInject("key") key: String,
        @JacksonInject("parent_path") path: String?
    ) : super(
        elements, key, path, NBTType.NBTTagByte,
        Float::class.java
    )

    constructor(other: QueryNodeList<Float>) : super(other)

    override fun copy(): QueryNodeListFloat {
        return QueryNodeListFloat(this)
    }
}
