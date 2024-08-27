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
import com.wolfyscript.scafall.config.jackson.KeyedBaseType
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import de.tr7zw.nbtapi.NBTType

@KeyedBaseType(baseType = QueryNode::class)
abstract class QueryNodePrimitive<VAL> : QueryNode<VAL> {
    protected val value: ValueProvider<VAL>

    @JsonCreator
    protected constructor(
        value: ValueProvider<VAL>,
        @JacksonInject("key") key: String,
        @JacksonInject("path") parentPath: String?
    ) : super(key, parentPath) {
        this.value = value
    }

    protected constructor(other: QueryNodePrimitive<VAL>) : super(other.key, other.parentPath) {
        this.nbtType = other.nbtType
        this.value = other.value
    }

    override fun check(key: String?, nbtType: NBTType, context: EvalContext, value: VAL): Boolean {
        return this.value.getValue(context) == value
    }
}
