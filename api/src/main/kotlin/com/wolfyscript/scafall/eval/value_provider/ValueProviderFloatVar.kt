package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "float/var")
class ValueProviderFloatVar @JsonCreator constructor(@JsonProperty("var") name: String) : ValueProviderVariable<Float>(Float::class.java, name), ValueProviderFloat
