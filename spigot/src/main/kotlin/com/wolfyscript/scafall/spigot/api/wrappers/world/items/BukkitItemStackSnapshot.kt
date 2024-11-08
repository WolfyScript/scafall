package com.wolfyscript.scafall.spigot.api.wrappers.world.items

import com.wolfyscript.scafall.common.api.data.CommonDataComponentMap
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.wrappers.BukkitRefAdapter
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot

class BukkitItemStackSnapshot(bukkitRef: org.bukkit.inventory.ItemStack) : BukkitRefAdapter<org.bukkit.inventory.ItemStack>(bukkitRef), ItemStackSnapshot {

    private val componentMap = CommonDataComponentMap(this.createStack()) // TODO: this is not how it was meant to work...

    override val item: Key = bukkitRef.type.key.api()
    override val amount: Int = bukkitRef.amount

    override fun createStack(): ItemStack = BukkitItemStack(bukkitRef)

    override fun data(): DataComponentMap<ItemStack> = componentMap

}