package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "list/int_array")
class NBTTagConfigListIntArray : NBTTagConfigListPrimitive<IntArray, NBTTagConfigIntArray> {
    @JsonCreator
    internal constructor(@JsonProperty("values") elements: List<NBTTagConfigIntArray>) : super(
        elements,
        NBTTagConfigIntArray::class.java
    )

    constructor(parent: NBTTagConfig?, elements: List<NBTTagConfigIntArray>) : super(
        parent,
        NBTTagConfigIntArray::class.java, elements
    )

    constructor(other: NBTTagConfigList<NBTTagConfigIntArray>) : super(other)

    override fun copy(): NBTTagConfigListIntArray {
        return NBTTagConfigListIntArray(this)
    }
}
