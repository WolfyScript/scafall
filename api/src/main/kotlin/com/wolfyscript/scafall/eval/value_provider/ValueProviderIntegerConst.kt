package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.eval.context.EvalContext
import java.io.IOException

@OptionalValueSerializer(serializer = ValueProviderIntegerConst.ValueSerializer::class)
@StaticNamespacedKey(key = "int/const")
class ValueProviderIntegerConst @JsonCreator constructor(
    @param:JsonProperty("value") override val value: Int
) : ValueProvider<Int>, ValueProviderInteger {

    override fun getValue(context: EvalContext): Int {
        return value
    }

    class ValueSerializer : com.wolfyscript.scafall.config.jackson.ValueSerializer<ValueProviderIntegerConst>(
        ValueProviderIntegerConst::class.java
    ) {
        @Throws(IOException::class)
        override fun serialize(
            valueProvider: ValueProviderIntegerConst,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            generator.writeNumber(valueProvider.value)
            return true
        }
    }
}
