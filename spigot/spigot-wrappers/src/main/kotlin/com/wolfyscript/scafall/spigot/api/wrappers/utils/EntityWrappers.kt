package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.wrappers.world.level.block.entity.ScafallBlockEntity
import com.wolfyscript.scafall.wrappers.minecraft.unwrap
import com.wolfyscript.scafall.wrappers.minecraft.wrap
import net.minecraft.world.level.block.entity.BlockEntity
import org.bukkit.block.TileState
import org.bukkit.craftbukkit.block.CraftBlockEntityState
import org.bukkit.craftbukkit.block.CraftBlockStates

/**
 * Wraps a Bukkit [TileState] to a Scafall [ScafallBlockEntity] if it is a valid block entity.
 *
 * @return An Scafall [ScafallBlockEntity] wrapper.
 * @throws IllegalStateException if the [TileState] is not a valid block entity.
 */
fun TileState.wrap(): ScafallBlockEntity {
    return this.into().wrap()
}

/**
 * Unwraps a Scafall [ScafallBlockEntity] to its corresponding Bukkit [TileState].
 *
 * @return The Bukkit [TileState].
 * @throws IllegalStateException if the [ScafallBlockEntity] cannot be unwrapped to a valid Bukkit [TileState].
 */
fun ScafallBlockEntity.unwrapSpigot(): TileState {
    return this.unwrap().into()
}

fun TileState.into(): BlockEntity {
    require(this is CraftBlockEntityState<*>) { "Cannot convert TileState of type ${this::class.simpleName} to Minecraft BlockEntity: Not a block entity!" }
    return block.level.getBlockEntity(block.position)
        ?: throw IllegalStateException("Cannot convert TileState to BlockEntity: BlockEntity not found in world!")
}

fun BlockEntity.into(): TileState {
    val blockState = CraftBlockStates.getBlockState(this.level?.world, this.blockPos, this.blockState, this)
    if (blockState is TileState) {
        return blockState
    }
    throw IllegalStateException("Cannot unwrap Block Entity ${this::class.simpleName} to TileState: Not a valid TileState!")
}

