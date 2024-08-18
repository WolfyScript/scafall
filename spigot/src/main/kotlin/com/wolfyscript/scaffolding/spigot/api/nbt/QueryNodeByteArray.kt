package com.wolfyscript.scaffolding.spigot.api.nbt

import com.wolfyscript.scaffolding.eval.context.EvalContext
import com.wolfyscript.scaffolding.eval.value_provider.ValueProvider
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import de.tr7zw.changeme.nbtapi.NBTType

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
        parent: de.tr7zw.changeme.nbtapi.NBTCompound
    ): ByteArray {
        return parent.getByteArray(key)
    }

    override fun applyValue(
        path: String,
        key: String,
        context: EvalContext,
        value: ByteArray,
        resultContainer: de.tr7zw.changeme.nbtapi.NBTCompound
    ) {
        resultContainer.setByteArray(key, value)
    }

    override fun copy(): QueryNodeByteArray {
        return QueryNodeByteArray(this)
    }
}
