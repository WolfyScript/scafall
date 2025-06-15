package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "long/var")
class ValueProviderLongVar @JsonCreator constructor(@JsonProperty("var") name: String) : ValueProviderVariable<Long>(Long::class.java, name), ValueProviderLong
