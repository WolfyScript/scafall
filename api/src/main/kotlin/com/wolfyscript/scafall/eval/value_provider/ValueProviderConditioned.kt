package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.operator.BoolOperator
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "conditioned")
class ValueProviderConditioned<V> @JsonCreator constructor(
    @param:JsonProperty(
        "condition"
    ) private val condition: BoolOperator,
    @field:JsonProperty("then") @param:JsonProperty("then") private val thenValue: ValueProvider<V>,
    @field:JsonProperty(
        "else"
    ) @param:JsonProperty(
        "else"
    ) private val elseValue: ValueProvider<V>
) : AbstractValueProvider<V>() {
    override fun getValue(context: EvalContext): V {
        return if (condition.evaluate(context)) thenValue.value else elseValue.value
    }
}
