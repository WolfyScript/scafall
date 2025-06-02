package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.common.api.wrappers.utils.CommonWrapperUtilsImpl
import com.wolfyscript.scafall.common.api.wrappers.world.items.ItemStackCommon
import com.wolfyscript.scafall.common.api.wrappers.world.items.ItemStackLikeCommon
import com.wolfyscript.scafall.common.api.wrappers.world.items.ItemStackSnapshotCommon
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.utils.snapshot
import com.wolfyscript.scafall.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.ScafallBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalPrecisePos
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import com.wolfyscript.scafall.wrappers.world.entity.Player
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.craftbukkit.util.CraftLocation
import org.bukkit.inventory.ItemStack

class SpigotWrapperUtilsImpl : CommonWrapperUtilsImpl(), SpigotWrapperUtils {

    //
    // ItemStacks
    //

    override fun wrapItemStack(spigotStack: ItemStack): com.wolfyscript.scafall.wrappers.world.items.ItemStack {
        return CraftItemStack.unwrap(spigotStack).wrap()
    }

    override fun wrapItemStackSnapshot(spigotStack: ItemStack): ItemStackSnapshot {
        return CraftItemStack.unwrap(spigotStack).snapshot()
    }

    override fun unwrapItemStack(wrappedStack: ItemStackLike<*, *>): ItemStack {
        if (wrappedStack !is ItemStackLikeCommon<*, *>) {
            throw IllegalArgumentException("Wrapped stack is not an instance of ${ItemStackLikeCommon::class.simpleName}")
        }

        return when (wrappedStack) {
            is ItemStackCommon -> {
                CraftItemStack.asCraftMirror(wrappedStack.mcStack)
            }

            is ItemStackSnapshotCommon -> {
                CraftItemStack.asBukkitCopy(wrappedStack.mcStack)
            }
        }
    }

    //
    // Position
    //

    override fun toPreciseGlobal(location: Location): ScafallGlobalPrecisePos? {
        if (location.world == null) {
            return null
        }
        return CraftLocation.toVec3(location).wrap(location.world.key.toAPI())
    }

    override fun toPrecise(location: Location): ScafallPrecisePos {
        return CraftLocation.toVec3(location).wrap()
    }

    override fun toBlockPos(location: Location): ScafallBlockPos {
        return CraftLocation.toBlockPosition(location).wrap()
    }

    override fun toBlockPosGlobal(location: Location): ScafallGlobalBlockPos? {
        if (location.world == null) {
            return null
        }
        return CraftLocation.toBlockPosition(location).wrap(location.world.key.toAPI())
    }

    //
    // Player
    //

    override fun wrapPlayer(player: org.bukkit.entity.Player): Player {
        return (player as CraftPlayer).handle.wrap()
    }

    override fun unwrapToSpigot(player: Player): org.bukkit.entity.Player? {
        return Bukkit.getPlayer(player.uuid)
    }

}