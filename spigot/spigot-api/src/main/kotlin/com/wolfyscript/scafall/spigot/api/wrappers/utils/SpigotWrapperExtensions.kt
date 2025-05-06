package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import org.bukkit.inventory.ItemStack

val Scafall.spigotWrapper: SpigotWrapperUtils
    get() {
        return this.minecraftWrapper as SpigotWrapperUtils
    }

fun ItemStack.wrap() : com.wolfyscript.scafall.wrappers.world.items.ItemStack = ScafallProvider.get().spigotWrapper.wrapItemStack(this)

fun <T: ItemStackLike<*, *>> T.unwrap(): ItemStack = ScafallProvider.get().spigotWrapper.unwrapItemStack(this)
