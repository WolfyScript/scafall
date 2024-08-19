package com.wolfyscript.scafall.spigot.api.nbt

import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProviderLong
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.changeme.nbtapi.NBTType

@StaticNamespacedKey(key = "long")
class QueryNodeLong : QueryNodePrimitive<Long> {
    @com.fasterxml.jackson.annotation.JsonCreator
    constructor(
        @com.fasterxml.jackson.annotation.JsonProperty("value") value: ValueProviderLong,
        @com.fasterxml.jackson.annotation.JacksonInject("key") key: String,
        @com.fasterxml.jackson.annotation.JacksonInject("parent_path") parentPath: String?
    ) : super(value, key, parentPath) {
        this.nbtType = NBTType.NBTTagLong
    }

    constructor(other: QueryNodePrimitive<Long>) : super(other)

    override fun readValue(
        path: String?,
        key: String?,
        parent: de.tr7zw.changeme.nbtapi.NBTCompound
    ): Long? {
        return parent.getLong(key)
    }

    override fun applyValue(
        path: String,
        key: String,
        context: EvalContext,
        value: Long,
        resultContainer: de.tr7zw.changeme.nbtapi.NBTCompound
    ) {
        resultContainer.setLong(key, value)
    }

    override fun copy(): QueryNodeLong {
        return QueryNodeLong(this)
    }
}
