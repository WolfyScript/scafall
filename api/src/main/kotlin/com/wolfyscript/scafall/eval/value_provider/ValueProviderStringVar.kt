package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "string/var")
class ValueProviderStringVar @JsonCreator constructor(@JsonProperty("var") name: String) : ValueProviderVariable<String>(String::class.java, name)
