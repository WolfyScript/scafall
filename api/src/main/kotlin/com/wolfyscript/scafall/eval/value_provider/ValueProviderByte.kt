package com.wolfyscript.scafall.eval.value_provider

import com.wolfyscript.scafall.eval.context.EvalContext


interface ValueProviderByte : ValueProvider<Byte> {
    override fun getValue(context: EvalContext): Byte
}
