package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.spigot.api.wrappers.world.items.BukkitItemStack
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.BukkitItemStackSnapshot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack

class SpigotWrapperUtilsImpl : SpigotWrapperUtils {

    override fun wrapItemStack(spigotStack: ItemStack): com.wolfyscript.scafall.wrappers.world.items.ItemStack {
        return BukkitItemStack(spigotStack)
    }

    override fun wrapItemStackSnapshot(spigotStack: ItemStack): ItemStackSnapshot {
        return BukkitItemStackSnapshot(spigotStack.clone())
    }

    override fun unwrapItemStack(wrappedStack: ItemStackLike<*, *>): ItemStack {
        return when (wrappedStack) {
            is BukkitItemStack -> {
                wrappedStack.bukkitRef
            }

            is BukkitItemStackSnapshot -> {
                // snapshots are immutable, so clone it to make sure we don't modify the original
                wrappedStack.bukkitRef.clone()
            }

            else -> throw Exception("Cannot unwrap ItemStackLike of type ${wrappedStack.javaClass}")
        }
    }

    override fun wrapMcStack(mcStack: net.minecraft.world.item.ItemStack): com.wolfyscript.scafall.wrappers.world.items.ItemStack {
        return BukkitItemStack(CraftItemStack.asCraftMirror(mcStack))
    }

    override fun wrapMcStackSnapshot(mcStack: net.minecraft.world.item.ItemStack): ItemStackSnapshot {
        return BukkitItemStackSnapshot(CraftItemStack.asBukkitCopy(mcStack))
    }

    override fun unwrapToMcStack(wrappedStack: ItemStackLike<*, *>): net.minecraft.world.item.ItemStack {
        return when (wrappedStack) {
            is BukkitItemStack -> {
                CraftItemStack.unwrap(wrappedStack.bukkitRef)
            }

            is BukkitItemStackSnapshot -> {
                CraftItemStack.asNMSCopy(wrappedStack.bukkitRef)
            }

            else -> throw Exception("Cannot unwrap ItemStackLike of type ${wrappedStack.javaClass}")
        }
    }

}