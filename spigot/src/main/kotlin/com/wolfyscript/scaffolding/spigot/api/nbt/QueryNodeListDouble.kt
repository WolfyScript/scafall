package com.wolfyscript.scaffolding.spigot.api.nbt

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import de.tr7zw.changeme.nbtapi.NBTType

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
