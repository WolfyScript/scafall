package com.wolfyscript.scafall.eval.value_provider

import com.wolfyscript.scafall.eval.context.EvalContext

interface ValueProviderInteger : ValueProvider<Int> {
    override fun getValue(context: EvalContext): Int
}
