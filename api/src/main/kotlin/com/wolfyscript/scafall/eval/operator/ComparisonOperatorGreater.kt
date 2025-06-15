package com.wolfyscript.scafall.eval.operator

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "greater")
class ComparisonOperatorGreater<V : Comparable<V>?> @JsonCreator protected constructor(
    @JsonProperty("this") thisValue: ValueProvider<V>,
    @JsonProperty("that") thatValue: ValueProvider<V>
) :
    ComparisonOperator<V>(thisValue, thatValue) {
    override fun evaluate(context: EvalContext): Boolean {
        return thisValue.getValue(context)!! > thatValue.getValue(context)
    }
}
