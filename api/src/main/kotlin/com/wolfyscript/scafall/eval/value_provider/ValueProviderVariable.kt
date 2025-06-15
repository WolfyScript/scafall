package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.eval.context.EvalContext

abstract class ValueProviderVariable<V> : ValueProvider<V> {
    @JsonProperty("var")
    private val variable: String

    @JsonIgnore
    private val typeClass: Class<V>

    protected constructor(typeClass: Class<V>, variable: String) : super() {
        this.typeClass = typeClass
        this.variable = variable
    }

    override fun getValue(context: EvalContext): V {
        return typeClass.cast(context.getVariable(variable))
    }
}
