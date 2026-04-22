package com.wolfyscript.scafall.eval.operator

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "and")
class LogicalOperatorAnd @JsonCreator constructor(
    @JsonProperty("this") thisValue: BoolOperator, @field:JsonProperty(
        "that"
    ) @param:JsonProperty("that") protected val thatValue: BoolOperator
) :
    LogicalOperator(thisValue) {
    override fun evaluate(context: EvalContext): Boolean {
        return thisValue.evaluate(context) && thatValue.evaluate(context)
    }
}
