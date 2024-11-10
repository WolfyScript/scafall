package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.data.DataKeyProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig

interface ItemsFactory {

    fun createStackConfig(itemKey: Key) : ItemStackConfig

    fun createFromSNBT(snbt: String) : ItemStack

    val dataKeyProvider: DataKeyProvider
}