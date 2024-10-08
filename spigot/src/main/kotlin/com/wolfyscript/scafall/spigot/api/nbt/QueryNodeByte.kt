package com.wolfyscript.scafall.spigot.api.nbt

import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.nbtapi.NBTType

@StaticNamespacedKey(key = "byte")
class QueryNodeByte : QueryNodePrimitive<Byte> {
    @com.fasterxml.jackson.annotation.JsonCreator
    constructor(
        @com.fasterxml.jackson.annotation.JsonProperty("value") value: ValueProvider<Byte>,
        @com.fasterxml.jackson.annotation.JacksonInject("key") key: String,
        @com.fasterxml.jackson.annotation.JacksonInject("parent_path") parentPath: String?
    ) : super(value, key, parentPath) {
        this.nbtType = NBTType.NBTTagByte
    }

    private constructor(other: QueryNodeByte) : super(other)

    override fun readValue(
        path: String?,
        key: String?,
        parent: de.tr7zw.nbtapi.NBTCompound
    ): Byte {
        return parent.getByte(key)
    }

    override fun applyValue(
        path: String,
        key: String,
        context: EvalContext,
        value: Byte,
        resultContainer: de.tr7zw.nbtapi.NBTCompound
    ) {
        resultContainer.setByte(key, value)
    }

    override fun copy(): QueryNodeByte {
        return QueryNodeByte(this)
    }
}
