package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.config.jackson.ValueSerializer
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.eval.value_provider.ValueProviderDoubleConst
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.io.IOException

@OptionalValueSerializer(serializer = NBTTagConfigDouble.OptionalValueSerializer::class)
@StaticNamespacedKey(key = "double")
class NBTTagConfigDouble : NBTTagConfigPrimitive<Double> {
    @JsonCreator
    internal constructor(@JsonProperty("value") value: ValueProvider<Double>) : super(value)

    constructor(parent: NBTTagConfig?, value: ValueProvider<Double>) : super(parent, value)

    private constructor(other: NBTTagConfigDouble) : super(other)

    override fun copy(): NBTTagConfigDouble {
        return NBTTagConfigDouble(this)
    }

    class OptionalValueSerializer : ValueSerializer<NBTTagConfigDouble>(NBTTagConfigDouble::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigDouble,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is ValueProviderDoubleConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
