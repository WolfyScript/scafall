package com.wolfyscript.scafall.eval.context

class EvalContext {
    private val variables: MutableMap<String, Any> = HashMap()

    fun getVariable(variableName: String): Any? {
        return variables[variableName]
    }

    fun setVariable(name: String, value: Any) {
        variables[name] = value
    }
}
