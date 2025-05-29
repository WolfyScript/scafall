package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalPrecisePos
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

val Scafall.spigotWrapper: SpigotWrapperUtils
    get() {
        return this.minecraftWrapper as SpigotWrapperUtils
    }

private val wrapper = ScafallProvider.get().spigotWrapper

fun ItemStack.wrap() : com.wolfyscript.scafall.wrappers.world.items.ItemStack = wrapper.wrapItemStack(this)

fun ItemStack.snapshot() : ItemStackSnapshot = wrapper.wrapItemStackSnapshot(this)

fun ItemStackLike<*, *>.unwrap(): ItemStack = wrapper.unwrapItemStack(this)

fun Player.wrap(): com.wolfyscript.scafall.wrappers.world.entity.Player = wrapper.wrapPlayer(this)

fun com.wolfyscript.scafall.wrappers.world.entity.Player.unwrap(): Player? = wrapper.unwrapToSpigot(this)

fun Location.toPreciseGlobal(): ScafallGlobalPrecisePos? = wrapper.toPreciseGlobal(this)

fun Location.toPrecise(): com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos? = wrapper.toPrecise(this)


