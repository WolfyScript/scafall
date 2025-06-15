package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "list/compound")
class NBTTagConfigListCompound : NBTTagConfigList<NBTTagConfigCompound> {
    @JsonCreator
    internal constructor(@JsonProperty("values") elements: List<NBTTagConfigCompound>) : super(
        elements,
        NBTTagConfigCompound::class.java
    )

    constructor(parent: NBTTagConfig?, elements: List<NBTTagConfigCompound>) : super(
        parent,
        NBTTagConfigCompound::class.java, elements
    )

    private constructor(other: NBTTagConfigListCompound) : super(other)

    override fun copy(): NBTTagConfigListCompound {
        return NBTTagConfigListCompound(this)
    }
}
