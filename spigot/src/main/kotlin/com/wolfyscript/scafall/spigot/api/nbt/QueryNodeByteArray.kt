package com.wolfyscript.scafall.spigot.api.nbt

import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.nbtapi.NBTType

@StaticNamespacedKey(key = "byte_array")
class QueryNodeByteArray : QueryNodePrimitive<ByteArray> {
    @com.fasterxml.jackson.annotation.JsonCreator
    constructor(
        @com.fasterxml.jackson.annotation.JsonProperty("value") value: ValueProvider<ByteArray>,
        @com.fasterxml.jackson.annotation.JacksonInject("key") key: String,
        @com.fasterxml.jackson.annotation.JacksonInject("parent_path") parentPath: String?
    ) : super(value, key, parentPath) {
        this.nbtType = NBTType.NBTTagByteArray
    }

    private constructor(other: QueryNodeByteArray) : super(other.value, other.key, other.parentPath)

    override fun readValue(
        path: String?,
        key: String?,
        parent: de.tr7zw.nbtapi.NBTCompound
    ): ByteArray {
        return parent.getByteArray(key) ?: ByteArray(0)
    }

    override fun applyValue(
        path: String,
        key: String,
        context: EvalContext,
        value: ByteArray,
        resultContainer: de.tr7zw.nbtapi.NBTCompound
    ) {
        resultContainer.setByteArray(key, value)
    }

    override fun copy(): QueryNodeByteArray {
        return QueryNodeByteArray(this)
    }
}
