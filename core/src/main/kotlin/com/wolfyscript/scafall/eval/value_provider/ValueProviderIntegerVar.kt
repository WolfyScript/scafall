package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "int/var")
class ValueProviderIntegerVar(@JsonProperty("var") variable: String) : ValueProviderVariable<Int>(Int::class.java, variable), ValueProviderInteger
