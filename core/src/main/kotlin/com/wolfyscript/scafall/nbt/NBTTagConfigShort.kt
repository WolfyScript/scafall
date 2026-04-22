package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.config.jackson.ValueSerializer
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.eval.value_provider.ValueProviderShortConst
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.nbt.NBTTagConfigShort.OptionalValueProvider
import java.io.IOException

@OptionalValueSerializer(serializer = OptionalValueProvider::class)
@StaticNamespacedKey(key = "short")
class NBTTagConfigShort : NBTTagConfigPrimitive<Short> {
    @JsonCreator
    internal constructor(@JsonProperty("value") value: ValueProvider<Short>) : super(value)

    constructor(parent: NBTTagConfig?, value: ValueProvider<Short>) : super(parent, value)

    constructor(other: NBTTagConfigPrimitive<Short>) : super(other)

    override fun copy(): NBTTagConfigShort {
        return NBTTagConfigShort(this)
    }

    class OptionalValueProvider :
        ValueSerializer<NBTTagConfigShort>(NBTTagConfigShort::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigShort,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is ValueProviderShortConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
