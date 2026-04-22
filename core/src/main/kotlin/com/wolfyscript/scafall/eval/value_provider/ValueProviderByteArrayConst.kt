package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.eval.context.EvalContext

@StaticNamespacedKey(key = "byte/array/const")
class ValueProviderByteArrayConst @JsonCreator constructor(
    @param:JsonProperty("value") override val value: ByteArray
) : ValueProvider<ByteArray> {

    override fun getValue(context: EvalContext): ByteArray {
        return value
    }

}
