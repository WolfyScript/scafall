package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.identifier.Key

abstract class ValueProviderVariable<V> : AbstractValueProvider<V> {
    @JsonProperty("var")
    private val variable: String

    @JsonIgnore
    private val typeClass: Class<V>

    protected constructor(key: Key, typeClass: Class<V>, variable: String) : super(key) {
        this.typeClass = typeClass
        this.variable = variable
    }

    protected constructor(typeClass: Class<V>, variable: String) : super() {
        this.typeClass = typeClass
        this.variable = variable
    }

    override fun getValue(context: EvalContext): V {
        return typeClass.cast(context.getVariable(variable))
    }
}
