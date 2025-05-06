package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.wrappers.utils.MinecraftWrapper
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import org.bukkit.inventory.ItemStack

interface SpigotWrapperUtils : MinecraftWrapper {

    fun wrapItemStack(spigotStack: ItemStack): com.wolfyscript.scafall.wrappers.world.items.ItemStack

    fun unwrapItemStack(wrappedStack: ItemStackLike<*, *>): ItemStack

}