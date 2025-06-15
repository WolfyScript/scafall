package com.wolfyscript.scafall.eval.operator

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "or")
class LogicalOperatorOr @JsonCreator constructor(
    @JacksonInject wolfyUtils: PluginWrapper, @JsonProperty("this") thisValue: BoolOperator, @field:JsonProperty(
        "that"
    ) @param:JsonProperty("that") protected val thatValue: BoolOperator
) :
    LogicalOperator(wolfyUtils, thisValue) {
    override fun evaluate(context: EvalContext): Boolean {
        return thisValue.evaluate(context) || thatValue.evaluate(context)
    }
}
