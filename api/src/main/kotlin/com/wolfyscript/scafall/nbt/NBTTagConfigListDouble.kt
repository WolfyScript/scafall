package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "list/double")
class NBTTagConfigListDouble : NBTTagConfigListPrimitive<Double, NBTTagConfigDouble> {
    @JsonCreator
    internal constructor(@JsonProperty("values") elements: List<NBTTagConfigDouble>) : super(
        elements,
        NBTTagConfigDouble::class.java
    )

    constructor(parent: NBTTagConfig?, elements: List<NBTTagConfigDouble>) : super(
        parent,
        NBTTagConfigDouble::class.java, elements
    )

    constructor(other: NBTTagConfigList<NBTTagConfigDouble>) : super(other)

    override fun copy(): NBTTagConfigListDouble {
        return NBTTagConfigListDouble(this)
    }
}
