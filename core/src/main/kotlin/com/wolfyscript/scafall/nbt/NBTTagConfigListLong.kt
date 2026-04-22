package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "list/long")
class NBTTagConfigListLong : NBTTagConfigListPrimitive<Long, NBTTagConfigLong> {
    @JsonCreator
    internal constructor(@JsonProperty("values") elements: List<NBTTagConfigLong>) : super(
        elements,
        NBTTagConfigLong::class.java
    )

    constructor(parent: NBTTagConfig?, elements: List<NBTTagConfigLong>) : super(
        parent,
        NBTTagConfigLong::class.java, elements
    )

    constructor(other: NBTTagConfigList<NBTTagConfigLong>) : super(other)

    override fun copy(): NBTTagConfigListLong {
        return NBTTagConfigListLong(this)
    }
}
