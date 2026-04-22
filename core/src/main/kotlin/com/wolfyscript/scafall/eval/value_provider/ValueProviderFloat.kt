package com.wolfyscript.scafall.eval.value_provider

import com.wolfyscript.scafall.eval.context.EvalContext

interface ValueProviderFloat : ValueProvider<Float> {
    override fun getValue(context: EvalContext): Float
}
