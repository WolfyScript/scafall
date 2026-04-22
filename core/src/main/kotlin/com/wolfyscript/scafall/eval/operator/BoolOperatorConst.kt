package com.wolfyscript.scafall.eval.operator

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.io.IOException

@OptionalValueSerializer(serializer = BoolOperatorConst.ValueSerializer::class)
@StaticNamespacedKey(key = "bool/const")
class BoolOperatorConst @JsonCreator constructor(
    @param:JsonProperty(
        "value"
    ) private val value: Boolean
) :
    BoolOperator() {
    override fun evaluate(context: EvalContext): Boolean {
        return value
    }

    class ValueSerializer :
        com.wolfyscript.scafall.config.jackson.ValueSerializer<BoolOperatorConst>(BoolOperatorConst::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: BoolOperatorConst,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            generator.writeBoolean(targetObject.value)
            return true
        }
    }
}
