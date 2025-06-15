package com.wolfyscript.scafall.eval.value_provider

import com.wolfyscript.scafall.eval.context.EvalContext

interface ValueProviderLong : ValueProvider<Long> {
    override fun getValue(context: EvalContext): Long
}
