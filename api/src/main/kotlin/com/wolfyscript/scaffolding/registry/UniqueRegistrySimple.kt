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
package com.wolfyscript.scaffolding.registry

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Keyed

/**
 * A simple registry, used for basic use cases.
 *
 * @param <V> The type of the value.
</V> */
class UniqueRegistrySimple<V : Keyed> : AbstractRegistry<BiMap<Key, V>, V> {
    constructor(namespacedKey: Key, registries: Registries) : super(
        namespacedKey,
        HashBiMap.create<Key, V>(),
        registries
    )

    constructor(namespacedKey: Key, registries: Registries, type: Class<V>) : super(
        namespacedKey,
        HashBiMap.create<Key, V>(),
        registries,
        type
    )

}
