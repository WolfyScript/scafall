package com.wolfyscript.scafall.spigot.api.nbt

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.nbtapi.NBTType

@StaticNamespacedKey(key = "list/long")
class QueryNodeListLong : QueryNodeList<Long> {
    constructor(
        @JsonProperty("elements") elements: List<Element<Long>>,
        @JacksonInject("key") key: String,
        @JacksonInject("parent_path") path: String?
    ) : super(
        elements, key, path, NBTType.NBTTagByte,
        Long::class.java
    )

    constructor(other: QueryNodeList<Long>) : super(other)

    override fun copy(): QueryNodeListLong {
        return QueryNodeListLong(this)
    }
}
