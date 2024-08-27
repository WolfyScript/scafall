package com.wolfyscript.scafall.spigot.api.nbt

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.nbtapi.NBTType

@StaticNamespacedKey(key = "list/double")
class QueryNodeListDouble : QueryNodeList<Double> {
    constructor(
        @JsonProperty("elements") elements: List<Element<Double>>,
        @JacksonInject("key") key: String,
        @JacksonInject("parent_path") path: String?
    ) : super(
        elements, key, path, NBTType.NBTTagByte,
        Double::class.java
    )

    constructor(other: QueryNodeList<Double>) : super(other)

    override fun copy(): QueryNodeListDouble {
        return QueryNodeListDouble(this)
    }
}
