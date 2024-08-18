package com.wolfyscript.scaffolding.spigot.api.nbt

import com.wolfyscript.scaffolding.eval.context.EvalContext
import com.wolfyscript.scaffolding.eval.value_provider.ValueProvider
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import de.tr7zw.changeme.nbtapi.NBTType

@StaticNamespacedKey(key = "int_array")
class QueryNodeIntArray : QueryNodePrimitive<IntArray> {
    @com.fasterxml.jackson.annotation.JsonCreator
    constructor(
        @com.fasterxml.jackson.annotation.JsonProperty("value") value: ValueProvider<IntArray>,
        @com.fasterxml.jackson.annotation.JacksonInject("key") key: String,
        @com.fasterxml.jackson.annotation.JacksonInject("parent_path") parentPath: String?
    ) : super(value, key, parentPath) {
        this.nbtType = NBTType.NBTTagByteArray
    }

    constructor(other: QueryNodeIntArray) : super(other.value, other.key, other.parentPath)

    override fun readValue(
        path: String?,
        key: String?,
        parent: de.tr7zw.changeme.nbtapi.NBTCompound
    ): IntArray? {
        return parent.getIntArray(key)
    }

    override fun applyValue(
        path: String,
        key: String,
        context: EvalContext,
        value: IntArray,
        resultContainer: de.tr7zw.changeme.nbtapi.NBTCompound
    ) {
        resultContainer.setIntArray(key, value)
    }

    override fun copy(): QueryNodeIntArray {
        return QueryNodeIntArray(this)
    }
}
