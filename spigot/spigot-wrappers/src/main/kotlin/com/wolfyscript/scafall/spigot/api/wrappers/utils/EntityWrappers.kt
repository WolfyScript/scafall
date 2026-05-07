package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.wrappers.world.level.block.entity.ScafallBlockEntity
import com.wolfyscript.scafall.wrappers.minecraft.unwrap
import com.wolfyscript.scafall.wrappers.minecraft.wrap
import org.bukkit.block.TileState
import org.bukkit.craftbukkit.block.CraftBlockEntityState
import org.bukkit.craftbukkit.block.CraftBlockStates

fun TileState.wrap(): ScafallBlockEntity {
    if (this is CraftBlockEntityState<*>) {
        val be = block.handle.getBlockEntity(block.position)
        if (be != null) {
            return be.wrap()
        }
    }
    throw IllegalStateException("Cannot wrap TileState of type ${this::class.simpleName}: Not a block entity!")
}

fun ScafallBlockEntity.unwrapSpigot(): TileState {
    val mcBlockEntity = this.unwrap()
    val blockState = CraftBlockStates.getBlockState(mcBlockEntity.level?.world, mcBlockEntity.blockPos, mcBlockEntity.blockState, mcBlockEntity)
    if (blockState != null) {
        if (blockState is TileState) {
            return blockState
        }
    }
    throw IllegalStateException("Cannot unwrap Block Entity ${this::class.simpleName} to TileState: Not a valid TileState!")
}

