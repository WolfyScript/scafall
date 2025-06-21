package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.ScafallBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalPrecisePos
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

val Scafall.spigotWrapper: SpigotWrapperUtils
    get() {
        return this.minecraftWrapper as SpigotWrapperUtils
    }

private val wrapper = ScafallProvider.get().spigotWrapper

fun Key.toSpigot() : org.bukkit.NamespacedKey {
    return org.bukkit.NamespacedKey(namespace, value)
}

fun NamespacedKey.wrap() : Key {
    return Key.key(namespace, key)
}

fun ItemStack.wrap() : com.wolfyscript.scafall.wrappers.world.items.ItemStack = wrapper.wrapItemStack(this)

fun ItemStack.snapshot() : ItemStackSnapshot = wrapper.wrapItemStackSnapshot(this)

fun ItemStackLike<*, *>.unwrap(): ItemStack = wrapper.unwrapItemStack(this)

fun ItemStackLike<*, *>.unwrapSpigot(): ItemStack = wrapper.unwrapItemStack(this)

fun Player.wrap(): com.wolfyscript.scafall.wrappers.world.entity.Player = wrapper.wrapPlayer(this)

fun com.wolfyscript.scafall.wrappers.world.entity.Player.unwrap(): Player? = wrapper.unwrapToSpigot(this)

fun com.wolfyscript.scafall.wrappers.world.entity.Player.unwrapSpigot(): Player? = wrapper.unwrapToSpigot(this)

/**
 * Unwraps the Bukkit [Location] to a [ScafallGlobalPrecisePos] (Vec3 linked to a Level)
 *
 * @return The global precise position; or null when [Location.world] is not available
 */
fun Location.toPreciseGlobal(): ScafallGlobalPrecisePos? = wrapper.toPreciseGlobal(this)

fun Location.toPrecise(): com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos = wrapper.toPrecise(this)

fun Location.toBlockPos(): ScafallBlockPos = wrapper.toBlockPos(this)

/**
 * Unwraps the Bukkit [Location] to a [ScafallGlobalBlockPos] (BlockPos linked to a Level)
 *
 * @return The global block position; or null when [Location.world] is not available
 */
fun Location.toBlockPosGlobal(): ScafallGlobalBlockPos? = wrapper.toBlockPosGlobal(this)


