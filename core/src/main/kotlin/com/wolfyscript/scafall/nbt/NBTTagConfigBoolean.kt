package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.wolfyscript.scafall.config.jackson.OptionalValueSerializer
import com.wolfyscript.scafall.config.jackson.ValueSerializer
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.operator.BoolOperator
import com.wolfyscript.scafall.eval.operator.BoolOperatorConst
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import java.io.IOException

@OptionalValueSerializer(serializer = NBTTagConfigBoolean.OptionalValueSerializer::class)
@StaticNamespacedKey(key = "bool")
class NBTTagConfigBoolean : NBTTagConfig {
    private val value: BoolOperator

    @JsonCreator
    internal constructor(@JsonProperty("value") value: BoolOperator) : super() {
        this.value = value
    }

    constructor(parent: NBTTagConfig?, value: BoolOperator) : super(parent) {
        this.value = value
    }

    private constructor(other: NBTTagConfigBoolean) : super() {
        this.value = other.value
    }

    fun getValue(context: EvalContext): Boolean {
        return value.evaluate(context)
    }

    fun getValue(): Boolean {
        return getValue(EvalContext())
    }

    override fun copy(): NBTTagConfigBoolean {
        return NBTTagConfigBoolean(this)
    }

    class OptionalValueSerializer : ValueSerializer<NBTTagConfigBoolean>(NBTTagConfigBoolean::class.java) {
        @Throws(IOException::class)
        override fun serialize(
            targetObject: NBTTagConfigBoolean,
            generator: JsonGenerator,
            provider: SerializerProvider
        ): Boolean {
            if (targetObject.value is BoolOperatorConst) {
                generator.writeObject(targetObject.value)
                return true
            }
            return false
        }
    }
}
