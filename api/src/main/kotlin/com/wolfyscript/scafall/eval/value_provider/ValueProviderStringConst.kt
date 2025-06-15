package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.io.IOException

@OptionalValueSerializer(serializer = ValueProviderStringConst.ValueSerializer::class)
@StaticNamespacedKey(key = "string/const")
class ValueProviderStringConst @JsonCreator constructor(
    @param:JsonProperty("value") override val value: String
) : AbstractValueProvider<String>() {

    override fun getValue(context: EvalContext): String {
        return value
    }

    class ValueSerializer : com.wolfyscript.scafall.config.jackson.ValueSerializer<ValueProviderStringConst>(ValueProviderStringConst::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            valueProvider: ValueProviderStringConst,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            generator.writeString(valueProvider.value)
            return true
        }
    }
}


