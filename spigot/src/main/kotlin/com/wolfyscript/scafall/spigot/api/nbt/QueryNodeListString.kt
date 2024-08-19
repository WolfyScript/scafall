package com.wolfyscript.scafall.spigot.api.nbt

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.changeme.nbtapi.NBTType

@StaticNamespacedKey(key = "list/string")
class QueryNodeListString : QueryNodeList<String> {
    constructor(
        @JsonProperty("elements") elements: List<Element<String>>,
        @JacksonInject("key") key: String,
        @JacksonInject("parent_path") path: String?
    ) : super(
        elements, key, path, NBTType.NBTTagByte,
        String::class.java
    )

    constructor(other: QueryNodeList<String>) : super(other)

    override fun copy(): QueryNodeListString {
        return QueryNodeListString(this)
    }
}
