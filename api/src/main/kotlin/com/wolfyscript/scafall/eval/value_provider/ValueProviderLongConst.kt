package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.eval.context.EvalContext
import java.io.IOException

@OptionalValueSerializer(serializer = ValueProviderLongConst.ValueSerializer::class)
@StaticNamespacedKey(key = "long/const")
class ValueProviderLongConst @JsonCreator constructor(
    @param:JsonProperty("value") override val value: Long
) : AbstractValueProvider<Long>(), ValueProviderLong {
    override fun getValue(context: EvalContext): Long {
        return value
    }

    class ValueSerializer : com.wolfyscript.scafall.config.jackson.ValueSerializer<ValueProviderLongConst>(
        ValueProviderLongConst::class.java
    ) {
        @Throws(IOException::class)
        override fun serialize(
            valueProvider: ValueProviderLongConst,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            generator.writeString("${valueProvider.value}L")
            return true
        }
    }
}
