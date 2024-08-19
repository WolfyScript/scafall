/*
 *       ____ _  _ ____ ___ ____ _  _ ____ ____ ____ ____ ___ _ _  _ ____
 *       |    |  | [__   |  |  | |\/| |    |__/ |__| |___  |  | |\ | | __
 *       |___ |__| ___]  |  |__| |  | |___ |  \ |  | |     |  | | \| |__]
 *
 *       CustomCrafting Recipe creation and management tool for Minecraft
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.wolfyscript.scafall.spigot.api.nbt

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.operator.BoolOperator
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import de.tr7zw.changeme.nbtapi.NBTCompound
import de.tr7zw.changeme.nbtapi.NBTList
import de.tr7zw.changeme.nbtapi.NBTType
import java.util.*

@StaticNamespacedKey(key = "bool")
class QueryNodeBoolean : QueryNode<Any> {
    private val value: BoolOperator

    @JsonCreator
    constructor(
        @JsonProperty("value") value: BoolOperator,
        @JacksonInject("key") key: String,
        @JacksonInject("parent_path") parentPath: String?
    ) : super(key, parentPath) {
        this.value = value
    }

    private constructor(other: QueryNodeBoolean) : super(other.key, other.parentPath) {
        this.value = other.value
    }

    override fun check(key: String?, nbtType: NBTType, context: EvalContext, value: Any): Boolean {
        return this.key == key && this.value.evaluate(context)
    }

    override fun readValue(path: String?, key: String?, parent: NBTCompound): Any? {
        val type: NBTType = parent.getType(key)
        return Optional.ofNullable<Any>(
            when (type) {
                NBTType.NBTTagInt -> parent.getInteger(key)
                NBTType.NBTTagIntArray -> parent.getIntArray(key)
                NBTType.NBTTagByte -> parent.getByte(key)
                NBTType.NBTTagByteArray -> parent.getByteArray(key)
                NBTType.NBTTagShort -> parent.getShort(key)
                NBTType.NBTTagLong -> parent.getLong(key)
                NBTType.NBTTagDouble -> parent.getDouble(key)
                NBTType.NBTTagFloat -> parent.getFloat(key)
                NBTType.NBTTagString -> parent.getString(key)
                NBTType.NBTTagCompound -> parent.getCompound(key)
                NBTType.NBTTagList -> getListOfType(parent.getListType(key), key, parent)
                else -> null
            }
        )
    }

    private fun getListOfType(nbtType: NBTType, key: String?, container: NBTCompound): NBTList<*> {
        return when (nbtType) {
            NBTType.NBTTagInt -> container.getIntegerList(key)
            NBTType.NBTTagIntArray -> container.getIntArrayList(key)
            NBTType.NBTTagLong -> container.getLongList(key)
            NBTType.NBTTagDouble -> container.getDoubleList(key)
            NBTType.NBTTagFloat -> container.getFloatList(key)
            NBTType.NBTTagString -> container.getStringList(key)
            NBTType.NBTTagCompound -> container.getCompoundList(key)
            else -> throw IllegalArgumentException("Cannot create NBTList of type $nbtType")
        }
    }

    override fun applyValue(path: String, key: String, context: EvalContext, value: Any, resultContainer: NBTCompound) {
        when(value) {
            is Int -> resultContainer.setInteger(key, value)
            is Byte -> resultContainer.setByte(key, value)
            is Short -> resultContainer.setShort(key, value)
            is Long -> resultContainer.setLong(key, value)
            is Double -> resultContainer.setDouble(key, value)
            is Float -> resultContainer.setFloat(key, value)
            is String -> resultContainer.setString(key, value)
            is IntArray -> resultContainer.setIntArray(key, value)
            is ByteArray -> resultContainer.setByteArray(key, value)
            is NBTCompound -> resultContainer.getOrCreateCompound(key).mergeCompound(value)
            is NBTList<*> -> {
                val nbtList: NBTList<*> = getListOfType(value.type, key, resultContainer)
                nbtList.clear()
                nbtList.addAll(value as Collection<Nothing>)
            }
        }
    }

    override fun copy(): QueryNodeBoolean {
        return QueryNodeBoolean(this)
    }
}
