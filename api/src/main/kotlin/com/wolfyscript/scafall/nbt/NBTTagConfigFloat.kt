package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.config.jackson.ValueSerializer
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.eval.value_provider.ValueProviderFloatConst
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.io.IOException

@OptionalValueSerializer(serializer = NBTTagConfigFloat.OptionalValueSerializer::class)
@StaticNamespacedKey(key = "float")
class NBTTagConfigFloat : NBTTagConfigPrimitive<Float> {
    @JsonCreator
    internal constructor(@JsonProperty("value") valueNode: ValueProvider<Float>) : super(valueNode)

    constructor(parent: NBTTagConfig?, value: ValueProvider<Float>) : super(parent, value)

    private constructor(other: NBTTagConfigFloat) : super(other)

    override fun copy(): NBTTagConfigFloat {
        return NBTTagConfigFloat(this)
    }

    class OptionalValueSerializer :
        ValueSerializer<NBTTagConfigFloat>(NBTTagConfigFloat::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigFloat,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is ValueProviderFloatConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
