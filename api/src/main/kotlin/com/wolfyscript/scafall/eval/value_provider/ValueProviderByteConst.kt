package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.io.IOException

@OptionalValueSerializer(serializer = ValueProviderByteConst.ValueSerializer::class)
@StaticNamespacedKey(key = "byte/const")
class ValueProviderByteConst @JsonCreator constructor(
    @param:JsonProperty("value") override val value: Byte
) : AbstractValueProvider<Byte>(), ValueProviderByte {
    override fun getValue(context: EvalContext): Byte {
        return value
    }

    class ValueSerializer : com.wolfyscript.scafall.config.jackson.ValueSerializer<ValueProviderByteConst>(
        ValueProviderByteConst::class.java
    ) {
        @Throws(IOException::class)
        override fun serialize(
            valueProvider: ValueProviderByteConst,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            generator.writeString(valueProvider.value.toString() + "b")
            return true
        }
    }
}
