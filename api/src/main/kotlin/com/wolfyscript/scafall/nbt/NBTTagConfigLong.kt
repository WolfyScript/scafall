package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.config.jackson.ValueSerializer
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.eval.value_provider.ValueProviderLong
import com.wolfyscript.scafall.eval.value_provider.ValueProviderLongConst
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.io.IOException

@OptionalValueSerializer(serializer = NBTTagConfigLong.OptionalValueSerializer::class)
@StaticNamespacedKey(key = "long")
class NBTTagConfigLong : NBTTagConfigPrimitive<Long> {
    @JsonCreator
    internal constructor(@JsonProperty("value") value: ValueProviderLong) : super(value)

    constructor(parent: NBTTagConfig?, value: ValueProvider<Long>) : super(parent, value)

    constructor(other: NBTTagConfigPrimitive<Long>) : super(other)

    override fun copy(): NBTTagConfigLong {
        return NBTTagConfigLong(this)
    }

    class OptionalValueSerializer :
        ValueSerializer<NBTTagConfigLong>(NBTTagConfigLong::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigLong,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is ValueProviderLongConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
