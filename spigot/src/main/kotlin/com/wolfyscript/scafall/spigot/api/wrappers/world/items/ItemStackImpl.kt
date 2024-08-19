package com.wolfyscript.scafall.spigot.api.wrappers.world.items

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.data.ItemStackDataComponentMap
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.wrappers.BukkitRefAdapter
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import org.bukkit.inventory.ItemStack

class ItemStackImpl(bukkitRef: ItemStack) : BukkitRefAdapter<ItemStack>(bukkitRef), com.wolfyscript.scafall.wrappers.world.items.ItemStack {
    private val componentMap = ItemStackDataComponentMap(this)

    override val item: Key
        get() {
            return bukkitRef.type.key.api()
        }

    override val amount: Int
        get() = bukkitRef.amount

    override fun snapshot(): ItemStackConfig {
        return BukkitItemStackConfig(this)
    }

    override fun data(): ItemStackDataComponentMap {
        return componentMap
    }
}
