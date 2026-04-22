package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "int_array")
class NBTTagConfigIntArray : NBTTagConfigPrimitive<IntArray> {
    @JsonCreator
    internal constructor(@JsonProperty("value") value: ValueProvider<IntArray>) : super(value)

    constructor(parent: NBTTagConfig?, value: ValueProvider<IntArray>) : super(parent, value)

    constructor(other: NBTTagConfigIntArray) : super(other.value)

    override fun copy(): NBTTagConfigIntArray {
        return NBTTagConfigIntArray(this)
    }
}
