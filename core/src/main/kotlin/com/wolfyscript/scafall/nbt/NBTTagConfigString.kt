package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.config.jackson.ValueSerializer
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.eval.value_provider.ValueProviderStringConst
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.io.IOException

@OptionalValueSerializer(serializer = NBTTagConfigString.OptionalValueSerializer::class)
@StaticNamespacedKey(key = "string")
class NBTTagConfigString : NBTTagConfigPrimitive<String> {
    @JsonCreator
    internal constructor(@JsonProperty("value") value: ValueProvider<String>) : super(value)

    constructor(parent: NBTTagConfig?, value: ValueProvider<String>) : super(parent, value)

    constructor(other: NBTTagConfigPrimitive<String>) : super(other)

    override fun copy(): NBTTagConfigString {
        return NBTTagConfigString(this)
    }

    class OptionalValueSerializer :
        ValueSerializer<NBTTagConfigString>(NBTTagConfigString::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigString,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is ValueProviderStringConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
