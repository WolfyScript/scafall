package com.wolfyscript.scafall.common.api.wrappers.utils

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.common.api.wrappers.ScafallBlockEntityCommon
import com.wolfyscript.scafall.common.api.wrappers.ScafallLevelCommon
import com.wolfyscript.scafall.common.api.wrappers.world.ScafallBlockPosCommon
import com.wolfyscript.scafall.common.api.wrappers.world.ScafallGlobalBlockPosCommon
import com.wolfyscript.scafall.common.api.wrappers.world.ScafallGlobalPrecisePosCommon
import com.wolfyscript.scafall.common.api.wrappers.world.ScafallPrecisePosCommon
import com.wolfyscript.scafall.common.api.wrappers.world.entity.ScafallPlayerCommon
import com.wolfyscript.scafall.common.api.wrappers.world.items.ItemStackCommon
import com.wolfyscript.scafall.common.api.wrappers.world.items.ItemStackLikeCommon
import com.wolfyscript.scafall.common.api.wrappers.world.items.ItemStackSnapshotCommon
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.ScafallBlockEntity
import com.wolfyscript.scafall.wrappers.ScafallLevel
import com.wolfyscript.scafall.wrappers.MinecraftWrapper
import com.wolfyscript.scafall.wrappers.unwrap
import com.wolfyscript.scafall.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.ScafallBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalPrecisePos
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import com.wolfyscript.scafall.wrappers.ScafallPlayer
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.phys.Vec3

abstract class CommonWrapperUtilsImpl : MinecraftWrapper {

    //
    // ItemStack
    //

    override fun wrapMcStack(mcStack: net.minecraft.world.item.ItemStack): com.wolfyscript.scafall.wrappers.world.items.ItemStack {
        return ItemStackCommon.fromVanilla(mcStack)
    }

    override fun wrapMcStackSnapshot(mcStack: net.minecraft.world.item.ItemStack): ItemStackSnapshot {
        return ItemStackSnapshotCommon(mcStack.copy())
    }

    override fun unwrapToMcStack(wrappedStack: ItemStackLike): net.minecraft.world.item.ItemStack {
        if (wrappedStack !is ItemStackLikeCommon) {
            throw IllegalArgumentException("Wrapped stack is not an instance of ${ItemStackLikeCommon::class.simpleName}")
        }

        return when (wrappedStack) {
            is ItemStackCommon -> {
                wrappedStack.mcStack
            }

            is ItemStackSnapshotCommon -> {
                wrappedStack.mcStack.copy()
            }
        }
    }

    //
    // Player
    //

    override fun wrapMcPlayer(player: net.minecraft.world.entity.player.Player): ScafallPlayer {
        return ScafallPlayerCommon(player)
    }

    override fun unwrapToMcPlayer(scafallPlayer: ScafallPlayer): net.minecraft.world.entity.player.Player? {
        return ScafallProvider.get().server.minecraftServer.playerList.getPlayer(scafallPlayer.uuid)
    }

    //
    // Position
    //

    override fun wrapGlobalBlockPos(position: GlobalPos): ScafallGlobalBlockPos {
        val dimension = position.dimension().location()
        return ScafallGlobalBlockPosCommon(Key.key(dimension.namespace, dimension.path), position.pos.wrap())
    }

    override fun wrapBlockPos(position: BlockPos): ScafallBlockPos {
        return ScafallBlockPosCommon(position)
    }

    override fun wrapBlockPos(
        position: BlockPos,
        dimension: Key,
    ): ScafallGlobalBlockPos {
        return ScafallGlobalBlockPosCommon(dimension, position.wrap())
    }

    override fun wrapVec3(vec: Vec3): ScafallPrecisePos {
        return ScafallPrecisePosCommon(vec)
    }

    override fun wrapVec3(
        vec: Vec3,
        dimension: Key,
    ): ScafallGlobalPrecisePos {
        return ScafallGlobalPrecisePosCommon(dimension, vec.wrap())
    }

    override fun unwrapPrecisePos(precisePos: ScafallPrecisePos): Vec3 {
        return Vec3(precisePos.x, precisePos.y, precisePos.z)
    }

    override fun unwrapGlobalPrecisePos(precisePos: ScafallGlobalPrecisePos): Pair<Vec3, ResourceKey<Level>> {
        return Pair(
            precisePos.pos.unwrap(), ResourceKey.create(
                Registries.DIMENSION,
                ResourceLocation.fromNamespaceAndPath(
                    precisePos.dimension.namespace,
                    precisePos.dimension.value
                )
            )
        )
    }

    override fun unwrapBlockPos(blockPos: ScafallBlockPos): BlockPos {
        return BlockPos(blockPos.x, blockPos.y, blockPos.z)
    }

    override fun unwrapGlobalBlockPos(globalBlockPos: ScafallGlobalBlockPos): GlobalPos {
        return GlobalPos.of(
            ResourceKey.create<Level>(
                Registries.DIMENSION,
                ResourceLocation.fromNamespaceAndPath(
                    globalBlockPos.dimension.namespace,
                    globalBlockPos.dimension.value
                )
            ), globalBlockPos.blockPos.unwrap()
        )
    }


    //
    // BlockEntity
    //

    override fun wrapBlockEntity(blockEntity: BlockEntity): ScafallBlockEntity {
        return ScafallBlockEntityCommon(blockEntity)
    }

    override fun unwrapBlockEntity(scafallBlockEntity: ScafallBlockEntity): BlockEntity {
        return (scafallBlockEntity as ScafallBlockEntityCommon).entity
    }

    //
    // Level
    //

    override fun wrapLevel(level: Level): ScafallLevel {
        return ScafallLevelCommon(level)
    }

    override fun unwrapLevel(scafallLevel: ScafallLevel): Level {
        return (scafallLevel as ScafallLevelCommon).level
    }

}