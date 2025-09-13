package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.ScafallPlayer
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

//
// Key converters
//

/**
 * Converts this [Key] to a Spigot [NamespacedKey]
 */
fun Key.toSpigot() : NamespacedKey {
    return org.bukkit.NamespacedKey(namespace, value)
}

/**
 * Converts this Spigot [NamespacedKey] to a scafall [Key]
 */
fun NamespacedKey.toScafall() : Key {
    return Key.key(namespace, key)
}

//
// ItemStack wrappers
//

/**
 * Wraps the Minecraft Stack of this Bukkit ItemStack in a scafall [ItemStack][com.wolfyscript.scafall.wrappers.world.items.ItemStack].
 *
 * #### **Warning!**
 *
 * Bukkit ItemStacks **may not have Minecraft ItemStack** associated with them! (e.g. when created via the [ItemStack] constructor)
 *
 * In those cases, the wrapped stack is **not linked to the original Bukkit stack**,
 * and **changes to the wrapped stack won't be reflected on the Bukkit stack!**
 *
 * #### Alternative
 * If a consistent behaviour is required use [snapshot] instead!
 *
 * @see snapshot
 */
fun ItemStack.wrap() : com.wolfyscript.scafall.wrappers.world.items.ItemStack = wrapper.wrapItemStack(this)

/**
 * Wraps the Minecraft Stack of this Bukkit ItemStack in a scafall [ItemStackSnapshot].
 *
 * _The warning of [ItemStack.wrap] does not apply here, because [ItemStackSnapshots][ItemStackSnapshot] are immutable (changes are never reflected on the original)_
 */
fun ItemStack.snapshot() : ItemStackSnapshot = wrapper.wrapItemStackSnapshot(this)

/**
 * Unwraps this item stack wrapper to a Bukkit [ItemStack]
 */
fun ItemStackLike.unwrapSpigot(): ItemStack = wrapper.unwrapItemStack(this)


//
// Player wrappers
//

/**
 * Wraps this [Player] in a scafall wrapper.
 */
fun Player.wrap(): ScafallPlayer = wrapper.wrapPlayer(this)

/**
 * Unwraps this wrapper to a Spigot/Bukkit [Player].
 */
fun ScafallPlayer.unwrapSpigot(): Player? = wrapper.unwrapToSpigot(this)

//
// Location wrappers
//

/**
 * Wraps this [Location] in a [ScafallGlobalPrecisePos] (PrecisePos with an associated Level)
 *
 * @return The global precise position; or null when [Location.world] is not available
 */
fun Location.toPreciseGlobal(): ScafallGlobalPrecisePos? = wrapper.toPreciseGlobal(this)

/**
 * Wraps this [Location] in a [com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos]
 */
fun Location.toPrecise(): com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos = wrapper.toPrecise(this)

/**
 * Wraps this [Location] in a [com.wolfyscript.scafall.wrappers.world.ScafallBlockPos]
 */
fun Location.toBlockPos(): ScafallBlockPos = wrapper.toBlockPos(this)

/**
 * Wraps this [Location] in a [ScafallGlobalBlockPos] (BlockPos with an associated Level)
 *
 * @return The global block position; or null when [Location.world] is not available
 */
fun Location.toBlockPosGlobal(): ScafallGlobalBlockPos? = wrapper.toBlockPosGlobal(this)


