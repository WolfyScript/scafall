package com.wolfyscript.scafall.spigot.api.nbt

import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProviderShort
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.nbtapi.NBTType

@StaticNamespacedKey(key = "short")
class QueryNodeShort : QueryNodePrimitive<Short> {
    @com.fasterxml.jackson.annotation.JsonCreator
    constructor(
        @com.fasterxml.jackson.annotation.JsonProperty("value") value: ValueProviderShort,
        @com.fasterxml.jackson.annotation.JacksonInject("key") key: String,
        @com.fasterxml.jackson.annotation.JacksonInject("parent_path") parentPath: String?
    ) : super(value, key, parentPath) {
        this.nbtType = NBTType.NBTTagShort
    }

    constructor(other: QueryNodePrimitive<Short>) : super(other)

    override fun readValue(
        path: String?,
        key: String?,
        parent: de.tr7zw.nbtapi.NBTCompound
    ): Short? {
        return parent.getShort(key)
    }

    override fun applyValue(
        path: String,
        key: String,
        context: EvalContext,
        value: Short,
        resultContainer: de.tr7zw.nbtapi.NBTCompound
    ) {
        resultContainer.setShort(key, value)
    }

    override fun copy(): QueryNodeShort {
        return QueryNodeShort(this)
    }
}
