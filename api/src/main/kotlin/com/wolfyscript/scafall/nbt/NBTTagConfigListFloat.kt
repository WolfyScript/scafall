package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "list/float")
class NBTTagConfigListFloat : NBTTagConfigListPrimitive<Float, NBTTagConfigFloat> {
    @JsonCreator
    internal constructor(@JsonProperty("values") elements: List<NBTTagConfigFloat>) : super(
        elements,
        NBTTagConfigFloat::class.java
    )

    constructor(parent: NBTTagConfig?, elements: List<NBTTagConfigFloat>) : super(
        parent,
        NBTTagConfigFloat::class.java, elements
    )

    constructor(other: NBTTagConfigList<NBTTagConfigFloat>) : super(other)

    override fun copy(): NBTTagConfigListFloat {
        return NBTTagConfigListFloat(this)
    }
}
