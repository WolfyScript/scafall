package com.wolfyscript.scafall.spigot.api.nbt

import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.nbtapi.NBTType

@StaticNamespacedKey(key = "string")
class QueryNodeString : QueryNodePrimitive<String> {
    @com.fasterxml.jackson.annotation.JsonCreator
    constructor(
        @com.fasterxml.jackson.annotation.JsonProperty("value") value: ValueProvider<String>,
        @com.fasterxml.jackson.annotation.JacksonInject("key") key: String,
        @com.fasterxml.jackson.annotation.JacksonInject("parent_path") parentPath: String?
    ) : super(value, key, parentPath) {
        this.nbtType = NBTType.NBTTagString
    }

    constructor(other: QueryNodePrimitive<String>) : super(other)

    override fun readValue(
        path: String?,
        key: String?,
        parent: de.tr7zw.nbtapi.NBTCompound
    ): String? {
        return parent.getString(key)
    }

    override fun applyValue(
        path: String,
        key: String,
        context: EvalContext,
        value: String,
        resultContainer: de.tr7zw.nbtapi.NBTCompound
    ) {
        resultContainer.setString(key, value)
    }

    override fun copy(): QueryNodeString {
        return QueryNodeString(this)
    }
}
