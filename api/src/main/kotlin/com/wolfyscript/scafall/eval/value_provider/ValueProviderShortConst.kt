package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.eval.context.EvalContext
import java.io.IOException

@OptionalValueSerializer(serializer = ValueProviderShortConst.ValueSerializer::class)
@StaticNamespacedKey(key = "short/const")
class ValueProviderShortConst @JsonCreator constructor(
    @param:JsonProperty(
        "value"
    ) override val value: Short
) : AbstractValueProvider<Short>(), ValueProviderShort {
    override fun getValue(context: EvalContext): Short {
        return value
    }

    class ValueSerializer : com.wolfyscript.scafall.config.jackson.ValueSerializer<ValueProviderShortConst>(
        ValueProviderShortConst::class.java
    ) {
        @Throws(IOException::class)
        override fun serialize(
            valueProvider: ValueProviderShortConst,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            generator.writeString("${valueProvider.value}s")
            return true
        }
    }
}
