package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.StaticNamespacedKey

@StaticNamespacedKey(key = "byte/var")
class ValueProviderByteVar @JsonCreator constructor(@JsonProperty("var") name: String) : ValueProviderVariable<Byte>(Byte::class.java, name), ValueProviderByte
