package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.wrappers.utils.MinecraftWrapper
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalPrecisePos
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import org.bukkit.Location
import org.bukkit.inventory.ItemStack

/**
 * Wrapping Utils to make it easier to wrap Bukkit objects.
 * Or making it possible to wrap in the first place when using it without NMS.
 */
interface SpigotWrapperUtils : MinecraftWrapper {

    fun wrapItemStack(spigotStack: ItemStack): com.wolfyscript.scafall.wrappers.world.items.ItemStack

    fun wrapItemStackSnapshot(spigotStack: ItemStack): ItemStackSnapshot

    fun unwrapItemStack(wrappedStack: ItemStackLike<*, *>): ItemStack

    //
    // Position
    //

    fun toPreciseGlobal(location: Location): ScafallGlobalPrecisePos?

    fun toPrecise(location: Location): com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos?

    //
    // Player
    //

    fun wrapPlayer(player: org.bukkit.entity.Player): com.wolfyscript.scafall.wrappers.world.entity.Player

    fun unwrapToSpigot(player: com.wolfyscript.scafall.wrappers.world.entity.Player): org.bukkit.entity.Player?

}