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
package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.itemsadder

import org.bukkit.inventory.ItemStack
import java.util.*

class CustomStackWrapper private constructor(private val item: dev.lone.itemsadder.api.CustomStack) : CustomStack {
    override val itemStack: ItemStack
        get() = item.itemStack

    override val namespace: String
        get() = item.namespace

    override val id: String
        get() = item.id

    override val namespacedID: String
        get() = item.namespacedID

    override val permission: String?
        get() = item.permission

    override fun hasPermission(): Boolean {
        return item.hasPermission()
    }

    override val isBlockAllEnchants: Boolean
        get() = item.isBlockAllEnchants

    override fun hasUsagesAttribute(): Boolean {
        return item.hasUsagesAttribute()
    }

    override fun reduceUsages(amount: Int) {
        item.reduceUsages(amount)
    }

    override var usages: Int
        get() = item.usages
        set(amount) {
            item.usages = amount
        }

    override fun hasCustomDurability(): Boolean {
        return item.hasCustomDurability()
    }

    override var durability: Int
        get() = item.durability
        set(durability) {
            item.durability = durability
        }

    override val maxDurability: Int
        get() = item.maxDurability

    override val isBlock: Boolean
        get() = item.isBlock

    companion object {
        fun wrapStack(customStack: dev.lone.itemsadder.api.CustomStack?): Optional<CustomStack> {
            return Optional.ofNullable(wrapNullableStack(customStack))
        }

        private fun wrapNullableStack(customStack: dev.lone.itemsadder.api.CustomStack?): CustomStackWrapper? {
            return if (customStack != null) CustomStackWrapper(customStack) else null
        }
    }
}
