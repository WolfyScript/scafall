package com.wolfyscript.scafall.spigot.api.nbt

import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.changeme.nbtapi.NBTType

@StaticNamespacedKey(key = "float")
class QueryNodeFloat : QueryNodePrimitive<Float> {
    @com.fasterxml.jackson.annotation.JsonCreator
    constructor(
        @com.fasterxml.jackson.annotation.JsonProperty("value") valueNode: ValueProvider<Float>,
        @com.fasterxml.jackson.annotation.JacksonInject("key") key: String,
        @com.fasterxml.jackson.annotation.JacksonInject("parent_path") parentPath: String?
    ) : super(valueNode, key, parentPath) {
        this.nbtType = NBTType.NBTTagFloat
    }

    private constructor(other: QueryNodeFloat) : super(other)

    override fun readValue(
        path: String?,
        key: String?,
        parent: de.tr7zw.changeme.nbtapi.NBTCompound
    ): Float? {
        return parent.getFloat(key)
    }

    override fun applyValue(
        path: String,
        key: String,
        context: EvalContext,
        value: Float,
        resultContainer: de.tr7zw.changeme.nbtapi.NBTCompound
    ) {
        resultContainer.setFloat(key, value)
    }

    override fun copy(): QueryNodeFloat {
        return QueryNodeFloat(this)
    }
}
