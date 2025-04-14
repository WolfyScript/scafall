package com.wolfyscript.scafall.spigot.api.factories

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.factories.ItemsFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import de.tr7zw.nbtapi.NBT
import org.bukkit.Material

class SpigotItemsFactoryImpl(scafall: Scafall) : ItemsFactory {

    override fun createStack(item: Key): ItemStack {
        val material = Material.matchMaterial(item.toString())
        return material?.let { org.bukkit.inventory.ItemStack.of(it).wrap() } ?: throw IllegalArgumentException("Cannot create stack of type $item")
    }

    override fun createFromSNBT(snbt: String): ItemStack {
        val nbt = NBT.parseNBT(snbt)
        val stack = NBT.itemStackFromNBT(nbt)
        return stack?.wrap() ?: throw IllegalArgumentException("Couldn't create stack from SNBT: $snbt")
    }

}