package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "double/var")
class ValueProviderDoubleVar @JsonCreator constructor(@JsonProperty("var") name: String) : ValueProviderVariable<Double>(Double::class.java, name), ValueProviderDouble
