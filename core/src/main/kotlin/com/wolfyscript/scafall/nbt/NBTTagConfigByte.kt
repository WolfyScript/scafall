package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.config.jackson.ValueSerializer
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.eval.value_provider.ValueProviderByteConst
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.io.IOException

@OptionalValueSerializer(serializer = NBTTagConfigByte.OptionalValueSerializer::class)
@StaticNamespacedKey(key = "byte")
class NBTTagConfigByte : NBTTagConfigPrimitive<Byte> {
    @JsonCreator
    internal constructor(@JsonProperty("value") value: ValueProvider<Byte>) : super(value)

    constructor(parent: NBTTagConfig?, value: ValueProvider<Byte>) : super(parent, value)

    private constructor(other: NBTTagConfigByte) : super(other)

    override fun copy(): NBTTagConfigByte {
        return NBTTagConfigByte(this)
    }

    class OptionalValueSerializer :
        ValueSerializer<NBTTagConfigByte>(NBTTagConfigByte::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigByte,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is ValueProviderByteConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
