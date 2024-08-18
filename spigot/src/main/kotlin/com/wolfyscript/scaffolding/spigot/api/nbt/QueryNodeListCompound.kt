package com.wolfyscript.scaffolding.spigot.api.nbt

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import de.tr7zw.changeme.nbtapi.NBTCompound
import de.tr7zw.changeme.nbtapi.NBTType

@StaticNamespacedKey(key = "list/compound")
class QueryNodeListCompound : QueryNodeList<NBTCompound> {
    constructor(
        @JsonProperty("elements") elements: List<Element<NBTCompound>>,
        @JacksonInject("key") key: String,
        @JacksonInject("parent_path") path: String?
    ) : super(
        elements, key, path, NBTType.NBTTagCompound,
        NBTCompound::class.java
    )

    private constructor(other: QueryNodeListCompound) : super(other)

    override fun copy(): QueryNodeListCompound {
        return QueryNodeListCompound(this)
    }
}
