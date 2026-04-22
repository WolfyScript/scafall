package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "list/int")
class NBTTagConfigListInt : NBTTagConfigListPrimitive<Int, NBTTagConfigInt> {
    @JsonCreator
    internal constructor(@JsonProperty("values") elements: List<NBTTagConfigInt>) : super(
        elements,
        NBTTagConfigInt::class.java
    )

    constructor(parent: NBTTagConfig?, elements: List<NBTTagConfigInt>) : super(
        parent,
        NBTTagConfigInt::class.java, elements
    )

    constructor(other: NBTTagConfigList<NBTTagConfigInt>) : super(other)

    override fun copy(): NBTTagConfigListInt {
        return NBTTagConfigListInt(this)
    }
}
