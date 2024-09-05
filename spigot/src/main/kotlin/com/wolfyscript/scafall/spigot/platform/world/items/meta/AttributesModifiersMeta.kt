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
package com.wolfyscript.scafall.spigot.platform.world.items.meta

import com.google.common.collect.Multimap
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.defaultKey
import com.wolfyscript.scafall.spigot.platform.world.items.CustomItem
import com.wolfyscript.scafall.spigot.platform.world.items.ItemBuilder
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier

class AttributesModifiersMeta : Meta(KEY) {
    override fun check(item: CustomItem, itemOther: ItemBuilder): Boolean {
        val metaOther = itemOther.itemMeta
        val meta = item.itemMeta
        if (meta.hasAttributeModifiers()) {
            return metaOther.hasAttributeModifiers() && compareModifiers(
                meta.attributeModifiers,
                metaOther.attributeModifiers
            )
        }
        return !metaOther.hasAttributeModifiers()
    }

    companion object {
        val KEY: Key = defaultKey("attributes_modifiers")

        private fun compareModifiers(
            first: Multimap<Attribute, AttributeModifier>?,
            second: Multimap<Attribute, AttributeModifier>?
        ): Boolean {
            return if (first != null && second != null) {
                first.entries().stream()
                    .allMatch { entry: Map.Entry<Attribute, AttributeModifier> ->
                        second.containsEntry(
                            entry.key,
                            entry.value
                        )
                    } && second.entries().stream()
                    .allMatch { entry: Map.Entry<Attribute, AttributeModifier> ->
                        first.containsEntry(
                            entry.key,
                            entry.value
                        )
                    }
            } else {
                false
            }
        }
    }
}
