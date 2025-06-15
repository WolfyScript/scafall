package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

abstract class NBTTagConfigListPrimitive<VAL, T : NBTTagConfigPrimitive<VAL>> : NBTTagConfigList<T> {
    @JsonCreator
    internal constructor(@JsonProperty("values") elements: List<T>, elementType: Class<T>) : super(
        elements,
        elementType
    )

    constructor(parent: NBTTagConfig?, elementType: Class<T>, values: List<T>) : super(parent, elementType, values)

    constructor(other: NBTTagConfigList<T>) : super(other)
}
