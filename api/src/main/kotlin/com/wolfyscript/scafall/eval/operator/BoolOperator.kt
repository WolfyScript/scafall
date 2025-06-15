package com.wolfyscript.scafall.eval.operator

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.wolfyscript.scafall.config.jackson.KeyedBaseType
import com.wolfyscript.scafall.config.jackson.OptionalValueDeserializer
import com.wolfyscript.scafall.config.jackson.ValueDeserializer
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.identifier.Key
import java.io.IOException

/**
 * An Operator that evaluates into a booleanish value.
 */
@KeyedBaseType(baseType = Operator::class)
@OptionalValueDeserializer(deserializer = BoolOperator.OptionalValueDeserializer::class)
abstract class BoolOperator : Operator {
    constructor(namespacedKey: Key) : super(namespacedKey)

    constructor() : super()

    abstract fun evaluate(context: EvalContext): Boolean

    class OptionalValueDeserializer : ValueDeserializer<BoolOperator>(BoolOperator::class.java) {

        @Throws(IOException::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): BoolOperator {
            val node = p.readValueAsTree<JsonNode>()
            return BoolOperatorConst(
                node.asBoolean()
            )
        }

    }
}
