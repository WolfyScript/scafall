package com.wolfyscript.scafall.eval.value_provider

import com.wolfyscript.scafall.eval.context.EvalContext

interface ValueProviderShort : ValueProvider<Short> {
    override fun getValue(context: EvalContext): Short
}
