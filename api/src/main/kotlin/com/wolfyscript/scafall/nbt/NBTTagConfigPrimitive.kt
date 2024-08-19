/*
 *       WolfyUtilities, APIs and Utilities for Minecraft Spigot plugins
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
package com.wolfyscript.scafall.nbt

import com.fasterxml.jackson.annotation.JsonCreator
import com.wolfyscript.scafall.config.jackson.KeyedBaseType
import com.wolfyscript.scafall.eval.value_provider.ValueProvider


@KeyedBaseType(baseType = NBTTagConfig::class)
abstract class NBTTagConfigPrimitive<VAL> : NBTTagConfig {
    val value: ValueProvider<VAL>

    @JsonCreator
    protected constructor(value: ValueProvider<VAL>) : super() {
        this.value = value
    }

    protected constructor(parent: NBTTagConfig?, value: ValueProvider<VAL>) : super(parent) {
        this.value = value
    }

    protected constructor(other: NBTTagConfigPrimitive<VAL>) : super() {
        this.value = other.value
    }
}
