package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(value = "wolfyutilities:short/var")
class ValueProviderShortVar @JsonCreator constructor(@JsonProperty("var") name: String) : ValueProviderVariable<Short>(Short::class.java, name), ValueProviderShort
