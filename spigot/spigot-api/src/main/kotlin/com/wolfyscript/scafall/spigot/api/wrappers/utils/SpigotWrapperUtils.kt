package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.wrappers.utils.MinecraftWrapper
import com.wolfyscript.scafall.wrappers.world.ScafallBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPos
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

    /**
     * Wraps the Minecraft Stack of the Bukkit ItemStack in a scafall [ItemStack][com.wolfyscript.scafall.wrappers.world.items.ItemStack].
     *
     * #### **Warning!**
     *
     * Bukkit ItemStacks **may not have Minecraft ItemStack** associated with them! (e.g. when created via the [ItemStack] constructor)
     *
     * In those cases, the wrapped stack is **not linked to the original Bukkit stack**,
     * and **changes to the wrapped stack won't be reflected on the Bukkit stack!**
     *
     */
    fun wrapItemStack(spigotStack: ItemStack): com.wolfyscript.scafall.wrappers.world.items.ItemStack

    /**
     * Wraps the Minecraft Stack of the Bukkit ItemStack in a scafall [ItemStackSnapshot].
     *
     * _The warning of [wrapItemStack] does not apply here, because [ItemStackSnapshots][ItemStackSnapshot] are immutable (changes are never reflected on the original)_
     */
    fun wrapItemStackSnapshot(spigotStack: ItemStack): ItemStackSnapshot

    /**
     * Unwraps the specified [ItemStackLike] to a Bukkit stack.
     */
    fun unwrapItemStack(wrappedStack: ItemStackLike<*, *>): ItemStack

    //
    // Position
    //

    fun toPreciseGlobal(location: Location): ScafallGlobalPrecisePos?

    fun toPrecise(location: Location): com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos

    fun toBlockPos(location: Location): ScafallBlockPos

    fun toBlockPosGlobal(location: Location): ScafallGlobalBlockPos?

    //
    // Player
    //

    fun wrapPlayer(player: org.bukkit.entity.Player): com.wolfyscript.scafall.wrappers.world.entity.Player

    fun unwrapToSpigot(player: com.wolfyscript.scafall.wrappers.world.entity.Player): org.bukkit.entity.Player?

}