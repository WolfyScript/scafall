package com.wolfyscript.scafall.eval.operator

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.PluginWrapper
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "equal")
class ComparisonOperatorEqual<V : Comparable<V>?> @JsonCreator protected constructor(
    @JacksonInject wolfyUtils: PluginWrapper,
    @JsonProperty("this") thisValue: ValueProvider<V>,
    @JsonProperty("that") thatValue: ValueProvider<V>
) :
    ComparisonOperator<V>(wolfyUtils, thisValue, thatValue) {
    override fun evaluate(context: EvalContext): Boolean {
        return thisValue.getValue(context)!!.compareTo(thatValue.getValue(context)) == 0
    }
}
