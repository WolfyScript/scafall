package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.eval.context.EvalContext
import java.io.IOException

@OptionalValueSerializer(serializer = ValueProviderDoubleConst.ValueSerializer::class)
@StaticNamespacedKey(key = "double/const")
class ValueProviderDoubleConst @JsonCreator constructor(
    @param:JsonProperty("value") override val value: Double
) : ValueProviderDouble {
    override fun getValue(context: EvalContext): Double {
        return value
    }

    class ValueSerializer : com.wolfyscript.scafall.config.jackson.ValueSerializer<ValueProviderDoubleConst>(
        ValueProviderDoubleConst::class.java
    ) {
        @Throws(IOException::class)
        override fun serialize(
            valueProvider: ValueProviderDoubleConst,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            generator.writeString("${valueProvider.value}d")
            return true
        }
    }
}
