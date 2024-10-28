package com.wolfyscript.scafall.spigot.api.factories

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.data.DataKeyProvider
import com.wolfyscript.scafall.factories.ItemsFactory
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.data.SpigotItemStackDataKeyProvider
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.BukkitItemStackConfig
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig

class SpigotItemsFactoryImpl(scafall: Scafall) : ItemsFactory {

    override fun createStackConfig(itemKey: Key): ItemStackConfig {
        return BukkitItemStackConfig(itemKey.toString())
    }

    override val dataKeyProvider: DataKeyProvider = SpigotItemStackDataKeyProvider(scafall)

}