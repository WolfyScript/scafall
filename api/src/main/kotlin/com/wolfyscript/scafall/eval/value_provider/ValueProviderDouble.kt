package com.wolfyscript.scafall.eval.value_provider

import com.wolfyscript.scafall.eval.context.EvalContext

interface ValueProviderDouble : ValueProvider<Double> {
    override fun getValue(context: EvalContext): Double
}
