package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "byte_array")
class NBTTagConfigByteArray : NBTTagConfigPrimitive<ByteArray> {
    @JsonCreator
    internal constructor(@JsonProperty("value") value: ValueProvider<ByteArray>) : super(value)

    constructor(parent: NBTTagConfig?, value: ValueProvider<ByteArray>) : super(parent, value)

    private constructor(other: NBTTagConfigByteArray) : super(other.value)

    override fun copy(): NBTTagConfigByteArray {
        return NBTTagConfigByteArray(this)
    }
}
