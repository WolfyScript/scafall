package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "list/string")
class NBTTagConfigListString : NBTTagConfigListPrimitive<String, NBTTagConfigString> {
    @JsonCreator
    internal constructor(@JsonProperty("values") elements: List<NBTTagConfigString>) : super(
        elements,
        NBTTagConfigString::class.java
    )

    constructor(parent: NBTTagConfig?, elements: List<NBTTagConfigString>) : super(
        parent,
        NBTTagConfigString::class.java, elements
    )

    constructor(other: NBTTagConfigList<NBTTagConfigString>) : super(other)

    override fun copy(): NBTTagConfigListString {
        return NBTTagConfigListString(this)
    }
}
