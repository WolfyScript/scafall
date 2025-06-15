package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.config.jackson.ValueSerializer
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.eval.value_provider.ValueProviderIntegerConst
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.io.IOException

@OptionalValueSerializer(serializer = NBTTagConfigInt.OptionalValueSerializer::class)
@StaticNamespacedKey(key = "int")
class NBTTagConfigInt : NBTTagConfigPrimitive<Int> {
    @JsonCreator
    internal constructor(@JsonProperty("value") value: ValueProvider<Int>) : super(value)

    constructor(parent: NBTTagConfig?, value: ValueProvider<Int>) : super(parent, value)

    constructor(other: NBTTagConfigPrimitive<Int>) : super(other)

    override fun copy(): NBTTagConfigPrimitive<Int> {
        return NBTTagConfigInt(this)
    }

    class OptionalValueSerializer :
        ValueSerializer<NBTTagConfigInt>(NBTTagConfigInt::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigInt,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is ValueProviderIntegerConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
