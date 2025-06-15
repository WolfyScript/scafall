package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.wolfyscript.scafall.config.jackson.KeyedBaseType
import com.wolfyscript.scafall.eval.value_provider.ValueProvider


@KeyedBaseType(baseType = NBTTagConfig::class)
abstract class NBTTagConfigPrimitive<VAL> : NBTTagConfig {
    val value: ValueProvider<VAL>

    @JsonCreator
    protected constructor(value: ValueProvider<VAL>) : super() {
        this.value = value
    }

    protected constructor(parent: NBTTagConfig?, value: ValueProvider<VAL>) : super(parent) {
        this.value = value
    }

    protected constructor(other: NBTTagConfigPrimitive<VAL>) : super() {
        this.value = other.value
    }
}
