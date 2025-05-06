package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.spigot.api.wrappers.world.items.BukkitItemStack
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.BukkitItemStackSnapshot
import com.wolfyscript.scafall.wrappers.utils.MinecraftWrapper
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

class SpigotWrapperUtilsImpl : SpigotWrapperUtils {

    override fun wrapItemStack(spigotStack: ItemStack): com.wolfyscript.scafall.wrappers.world.items.ItemStack {
        return BukkitItemStack(spigotStack)
    }

    override fun unwrapItemStack(wrappedStack: ItemStackLike<*, *>): ItemStack {
        return when (wrappedStack) {
            is BukkitItemStack -> { wrappedStack.bukkitRef }
            is BukkitItemStackSnapshot -> { wrappedStack.bukkitRef }
            else -> throw Exception("Cannot unwrap ItemStackLike of type ${wrappedStack.javaClass}")
        }
    }

    override fun wrapMcStack(mcStack: net.minecraft.world.item.ItemStack): com.wolfyscript.scafall.wrappers.world.items.ItemStack {
        return BukkitItemStack(CraftItemStack.asCraftMirror(mcStack))
    }

    override fun unwrapToMcStack(wrappedStack: ItemStackLike<*, *>): net.minecraft.world.item.ItemStack {
        return unwrapItemStack(wrappedStack).let { (it as CraftItemStack).handle }
    }

}