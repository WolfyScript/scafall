package com.wolfyscript.scaffolding.spigot.api.nbt

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import de.tr7zw.changeme.nbtapi.NBTType

@StaticNamespacedKey(key = "list/int")
class QueryNodeListInt : QueryNodeList<Int> {
    constructor(
        @JsonProperty("elements") elements: List<Element<Int>>,
        @JacksonInject("key") key: String,
        @JacksonInject("parent_path") path: String?
    ) : super(
        elements, key, path, NBTType.NBTTagByte,
        Int::class.java
    )

    constructor(other: QueryNodeList<Int>) : super(other)

    override fun copy(): QueryNodeListInt {
        return QueryNodeListInt(this)
    }
}
