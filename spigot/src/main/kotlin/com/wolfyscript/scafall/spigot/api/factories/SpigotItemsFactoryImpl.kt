package com.wolfyscript.scafall.spigot.api.factories

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.data.DataKeyProvider
import com.wolfyscript.scafall.factories.ItemsFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.data.SpigotItemStackDataKeyProvider
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.BukkitItemStackConfig
import com.wolfyscript.scafall.spigot.api.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import de.tr7zw.nbtapi.NBT

class SpigotItemsFactoryImpl(scafall: Scafall) : ItemsFactory {

    override fun createStackConfig(itemKey: Key): ItemStackConfig {
        return BukkitItemStackConfig(itemKey.toString())
    }

    override fun createFromSNBT(snbt: String): ItemStack {
        val nbt = NBT.parseNBT(snbt)
        val stack = NBT.itemStackFromNBT(nbt)
        return stack?.wrap() ?: throw IllegalArgumentException("Couldn't create stack from SNBT: $snbt")
    }

    override val dataKeyProvider: DataKeyProvider = SpigotItemStackDataKeyProvider(scafall)

}