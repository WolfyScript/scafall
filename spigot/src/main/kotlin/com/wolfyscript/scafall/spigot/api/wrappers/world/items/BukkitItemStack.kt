package com.wolfyscript.scafall.spigot.api.wrappers.world.items

import com.wolfyscript.scafall.common.api.data.ItemStackDataComponentMap
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.wrappers.BukkitRefAdapter
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import de.tr7zw.nbtapi.NBT
import org.bukkit.inventory.ItemStack
import java.io.ByteArrayOutputStream

class BukkitItemStack(bukkitRef: ItemStack) : BukkitRefAdapter<ItemStack>(bukkitRef), com.wolfyscript.scafall.wrappers.world.items.ItemStack {

    private val componentMap = ItemStackDataComponentMap(this)

    override val item: Key = bukkitRef.type.key.api()
    override val amount: Int = bukkitRef.amount

    override fun toNBTString(): String {
        return NBT.itemStackToNBT(bukkitRef).toString()
    }

    override fun toNBTBytes(): ByteArray {
        val stream = ByteArrayOutputStream()
        stream.use {
            NBT.itemStackToNBT(bukkitRef).writeCompound(it)
        }
        return stream.toByteArray()
    }

    override fun snapshot(): ItemStackSnapshot {
        return BukkitItemStackSnapshot(bukkitRef.clone())
    }

    override fun data(): DataComponentMap.Mutable<com.wolfyscript.scafall.wrappers.world.items.ItemStack> = componentMap

}
