package com.wolfyscript.scafall.spigot.api.wrappers.world.items

import com.wolfyscript.scafall.common.api.data.CommonDataComponentMap
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.wrappers.BukkitRefAdapter
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import org.bukkit.inventory.ItemStack

class BukkitItemStack(bukkitRef: ItemStack) : BukkitRefAdapter<ItemStack>(bukkitRef), com.wolfyscript.scafall.wrappers.world.items.ItemStack {

    private val componentMap = CommonDataComponentMap(this)

    override val item: Key = bukkitRef.type.key.api()
    override val amount: Int = bukkitRef.amount

    override fun snapshot(): ItemStackSnapshot {
        return BukkitItemStackSnapshot(bukkitRef)
    }

    override fun data(): DataComponentMap<com.wolfyscript.scafall.wrappers.world.items.ItemStack> = componentMap

}
