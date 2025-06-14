package com.wolfyscript.scafall.factories

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import net.minecraft.SharedConstants

interface ItemsFactory {

    fun createStack(item: Key) : ItemStack

    fun createFromSNBT(snbt: String) : ItemStack

    fun parseFromSNBT(snbt: String, fromVersion: Int, toVersion: Int = SharedConstants.getCurrentVersion().dataVersion.version) : ItemStack

}