package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.data.DataKeyProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.ItemStack

interface ItemsFactory {

    fun createStack(item: Key) : ItemStack

    fun createFromSNBT(snbt: String) : ItemStack

    val dataKeyProvider: DataKeyProvider
}