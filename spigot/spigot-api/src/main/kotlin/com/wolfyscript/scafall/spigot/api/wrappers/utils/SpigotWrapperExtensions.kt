package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.ScafallBlockEntity
import com.wolfyscript.scafall.wrappers.ScafallPlayer
import com.wolfyscript.scafall.wrappers.minecraft.snapshot
import com.wolfyscript.scafall.wrappers.minecraft.unwrap
import com.wolfyscript.scafall.wrappers.minecraft.wrap
import com.wolfyscript.scafall.wrappers.world.ScafallBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalPrecisePos
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLikeCommon
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshotCommon
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStackCommon
import net.minecraft.world.phys.Vec3
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.block.TileState
import org.bukkit.craftbukkit.block.CraftBlockEntityState
import org.bukkit.craftbukkit.block.CraftBlockStates
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.craftbukkit.util.CraftLocation
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Field

/**
 * A little reflection is necessary to get direct access to the handle of the CraftItemStack
 */
private val craftStackHandleField: Field? = try {
    val field = CraftItemStack::class.java.getDeclaredField("handle")
    field.isAccessible = true
    field
} catch (e: ReflectiveOperationException) {
    ScafallProvider.get().logger.error(
        "Failed to get the handle field from CraftItemStack! Please report this issue to the Scafall GitHub page!",
        e
    )
    null
}

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
 * Wraps the Minecraft Stack of this Bukkit ItemStack in a scafall [ItemStack][com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack].
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
fun ItemStack.wrap() : com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack {
    // Note ItemStacks may not be CraftItemStacks (e.g. created via ItemStack constructor).
    // In that case, we simply create the NMS stack copy of it. However, changes to the wrapped stack won't apply to the original!
    val craftStack = this as? CraftItemStack ?: return CraftItemStack.asNMSCopy(this).wrap()
    // When it is a CraftItemStack, we need to use a little reflection to access the handle.
    if (craftStackHandleField != null) {
        return (craftStackHandleField.get(craftStack) as net.minecraft.world.item.ItemStack).wrap()
    }
    // or fallback to a copy if field is not available for whatever reason
    return CraftItemStack.asNMSCopy(craftStack).wrap()
}

/**
 * Wraps the Minecraft Stack of this Bukkit ItemStack in a scafall [ItemStackSnapshot].
 *
 * _The warning of [ItemStack.wrap] does not apply here, because [ItemStackSnapshots][ItemStackSnapshot] are immutable (changes are never reflected on the original)_
 */
fun ItemStack.snapshot() : ItemStackSnapshot {
    return CraftItemStack.asNMSCopy(this).snapshot()
}

/**
 * Unwraps this item stack wrapper to a Bukkit [ItemStack]
 */
fun ItemStackLike.unwrapSpigot(): ItemStack {
    if (this !is ItemStackLikeCommon) {
        throw IllegalArgumentException("Wrapped stack is not an instance of ${ItemStackLikeCommon::class.simpleName}")
    }

    return when (this) {
        is ScafallItemStackCommon -> {
            CraftItemStack.asCraftMirror(mcStack)
        }

        is ItemStackSnapshotCommon -> {
            CraftItemStack.asBukkitCopy(mcStack)
        }
    }
}

//
// Player wrappers
//

/**
 * Wraps this [Player] in a scafall wrapper.
 */
fun Player.wrap(): ScafallPlayer {
    return (player as CraftPlayer).handle.wrap()
}

/**
 * Unwraps this wrapper to a Spigot/Bukkit [Player].
 */
fun ScafallPlayer.unwrapSpigot(): Player? {
    return Bukkit.getPlayer(uuid)
}

//
// Location wrappers
//

/**
 * Wraps this [Location] in a [ScafallGlobalPrecisePos] (PrecisePos with an associated Level)
 *
 * @return The global precise position; or null when [Location.world] is not available
 */
fun Location.toPreciseGlobal(): ScafallGlobalPrecisePos? {
    if (world == null) {
        return null
    }
    return Vec3(x, y, z).wrap(world.key.toScafall())
}

/**
 * Wraps this [Location] in a [com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos]
 */
fun Location.toPrecise(): com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos {
    return Vec3(x, y, z).wrap()
}

/**
 * Wraps this [Location] in a [com.wolfyscript.scafall.wrappers.world.ScafallBlockPos]
 */
fun Location.toBlockPos(): ScafallBlockPos {
    return CraftLocation.toBlockPosition(this).wrap()
}

/**
 * Wraps this [Location] in a [ScafallGlobalBlockPos] (BlockPos with an associated Level)
 *
 * @return The global block position; or null when [Location.world] is not available
 */
fun Location.toBlockPosGlobal(): ScafallGlobalBlockPos? {
    if (world == null) {
        return null
    }
    return CraftLocation.toBlockPosition(this).wrap(world.key.toScafall())
}

//
// Block Entity Wrappers
//

fun TileState.wrap(): ScafallBlockEntity {
    if (this is CraftBlockEntityState<*>) {
        val be = block.handle.getBlockEntity(block.position)
        if (be != null) {
            return be.wrap()
        }
    }
    throw IllegalStateException("Cannot wrap TileState of type ${this::class.simpleName}: Not a block entity!")
}

fun ScafallBlockEntity.unwrapToSpigot(): TileState {
    val mcBlockEntity = this.unwrap()
    val blockState = CraftBlockStates.getBlockState(mcBlockEntity.level?.world, mcBlockEntity.blockPos, mcBlockEntity.blockState, mcBlockEntity)
    if (blockState != null) {
        if (blockState is TileState) {
            return blockState
        }
    }
    throw IllegalStateException("Cannot unwrap Block Entity ${this::class.simpleName} to TileState: Not a valid TileState!")
}

