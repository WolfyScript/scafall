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
package com.wolfyscript.scafall.eval.value_provider

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.parse
import com.wolfyscript.scafall.identifier.StaticNamespacedKey.KeyBuilder.createKeyString

abstract class AbstractValueProvider<V> : ValueProvider<V> {
    @JsonIgnore
    protected val key: Key

    protected constructor(key: Key) {
        this.key = key
    }

    protected constructor() {
        this.key = parse(createKeyString(javaClass))
    }

    @JsonIgnore
    override fun key(): Key = key
}
