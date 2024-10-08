package com.wolfyscript.scafall.spigot.api.nbt

import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.nbtapi.NBTType

@StaticNamespacedKey(key = "double")
class QueryNodeDouble : QueryNodePrimitive<Double> {
    @com.fasterxml.jackson.annotation.JsonCreator
    constructor(
        @com.fasterxml.jackson.annotation.JsonProperty("value") value: ValueProvider<Double>,
        @com.fasterxml.jackson.annotation.JacksonInject("key") key: String,
        @com.fasterxml.jackson.annotation.JacksonInject("parent_path") parentPath: String?
    ) : super(value, key, parentPath) {
        this.nbtType = NBTType.NBTTagDouble
    }

    private constructor(other: QueryNodeDouble) : super(other)

    override fun readValue(
        path: String?,
        key: String?,
        parent: de.tr7zw.nbtapi.NBTCompound
    ): Double? {
        return parent.getDouble(key)
    }

    override fun applyValue(
        path: String,
        key: String,
        context: EvalContext,
        value: Double,
        resultContainer: de.tr7zw.nbtapi.NBTCompound
    ) {
        resultContainer.setDouble(key, value)
    }

    override fun copy(): QueryNodeDouble {
        return QueryNodeDouble(this)
    }
}
