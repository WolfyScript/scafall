package com.wolfyscript.scafall.wrappers.utils

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.ScafallBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalPrecisePos
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import com.wolfyscript.scafall.wrappers.world.entity.Player
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

/**
 * Wrapping utils that can wrap or unwrap to Minecraft's internal types.
 *
 * This may use a direct approach (Fabric) or use intermediate platform (Spigot, Sponge) wrappers to access the internal value.
 * For platforms like Sponge or Spigot, there exist separate api modules to wrap/unwrap those wrapper types instead of Minecraft's.
 */
interface MinecraftWrapper {

    //
    // ItemStacks
    //

    /**
     * Wraps a Minecraft ItemStack into a Scafall ItemStack.
     *
     * @param mcStack The Minecraft ItemStack to wrap
     */
    fun wrapMcStack(mcStack: net.minecraft.world.item.ItemStack): ItemStack

    fun wrapMcStackSnapshot(mcStack: net.minecraft.world.item.ItemStack): ItemStackSnapshot

    /**
     * Unwraps a Scafall ItemStack into a Minecraft ItemStack.
     *
     * @param wrappedStack The Scafall [ItemStack] or [ItemStackSnapshot][com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot] to unwrap
     */
    fun unwrapToMcStack(wrappedStack: ItemStackLike<*,*>): net.minecraft.world.item.ItemStack

    //
    // Player
    //

    fun wrapMcPlayer(player: net.minecraft.world.entity.player.Player): Player

    fun unwrapToMcPlayer(player: Player): net.minecraft.world.entity.player.Player?

    //
    // Position
    //

    fun wrapGlobalBlockPos(position: GlobalPos): ScafallGlobalBlockPos

    fun wrapBlockPos(position: BlockPos): ScafallBlockPos

    fun wrapBlockPos(position: BlockPos, dimension: Key): ScafallGlobalBlockPos

    fun wrapVec3(vec: Vec3): ScafallPrecisePos

    fun wrapVec3(vec: Vec3, dimension: Key): ScafallGlobalPrecisePos

    fun unwrapPrecisePos(precisePos: ScafallPrecisePos): Vec3

    fun unwrapGlobalPrecisePos(precisePos: ScafallGlobalPrecisePos): Pair<Vec3, ResourceKey<Level>>

    fun unwrapBlockPos(blockPos: ScafallBlockPos): BlockPos

    fun unwrapGlobalBlockPos(globalBlockPos: ScafallGlobalBlockPos): GlobalPos

}

//
// ItemStacks
//

/**
 * Wraps this Minecraft ItemStack in a scafall [ItemStack].
 *
 * **Any changes** made to the wrapped stack, once unwrapped, **are reflected on this original stack**.
 */
fun net.minecraft.world.item.ItemStack.wrap(): ItemStack = ScafallProvider.get().minecraftWrapper.wrapMcStack(this)

/**
 * Wraps a snapshot of this ItemStack in a scafall [ItemStackSnapshot]
 *
 * **Changes** made to the wrapped stack, once unwrapped, **won't be reflected on this stack**!
 */
fun net.minecraft.world.item.ItemStack.snapshot(): ItemStackSnapshot = ScafallProvider.get().minecraftWrapper.wrapMcStackSnapshot(this)

/**
 * Unwraps a scafall [ItemStackLike] to a minecraft [net.minecraft.world.item.ItemStack]
 */
fun ItemStackLike<*, *>.unwrap(): net.minecraft.world.item.ItemStack = ScafallProvider.get().minecraftWrapper.unwrapToMcStack(this)

//
// Player
//

fun net.minecraft.world.entity.player.Player.wrap(): Player = ScafallProvider.get().minecraftWrapper.wrapMcPlayer(this)

/**
 * Unwraps the Player to the Minecraft Player.
 * @return The minecraft Player; null if the player is no longer available.
 */
fun Player.unwrap(): net.minecraft.world.entity.player.Player? = ScafallProvider.get().minecraftWrapper.unwrapToMcPlayer(this)

//
// Position
//

fun GlobalPos.wrap(): ScafallGlobalBlockPos = ScafallProvider.get().minecraftWrapper.wrapGlobalBlockPos(this)

fun BlockPos.wrap(): ScafallBlockPos = ScafallProvider.get().minecraftWrapper.wrapBlockPos(this)

fun BlockPos.wrap(dimension: Key) = ScafallProvider.get().minecraftWrapper.wrapBlockPos(this, dimension)

fun Vec3.wrap() = ScafallProvider.get().minecraftWrapper.wrapVec3(this)

fun Vec3.wrap(dimension: Key) = ScafallProvider.get().minecraftWrapper.wrapVec3(this, dimension)

// unwrap

fun ScafallGlobalBlockPos.unwrap(): GlobalPos = ScafallProvider.get().minecraftWrapper.unwrapGlobalBlockPos(this)

fun ScafallGlobalPrecisePos.unwrap(): Pair<Vec3, ResourceKey<Level>> = ScafallProvider.get().minecraftWrapper.unwrapGlobalPrecisePos(this)

fun ScafallBlockPos.unwrap(): BlockPos = ScafallProvider.get().minecraftWrapper.unwrapBlockPos(this)

fun ScafallPrecisePos.unwrap(): Vec3 = ScafallProvider.get().minecraftWrapper.unwrapPrecisePos(this)

