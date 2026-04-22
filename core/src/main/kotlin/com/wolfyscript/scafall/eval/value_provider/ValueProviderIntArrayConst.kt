package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.eval.context.EvalContext

@StaticNamespacedKey(key = "int/array/const")
class ValueProviderIntArrayConst @JsonCreator constructor(
    @param:JsonProperty("value") override val value: IntArray
) : ValueProvider<IntArray> {

    override fun getValue(context: EvalContext): IntArray {
        return value
    }

}
