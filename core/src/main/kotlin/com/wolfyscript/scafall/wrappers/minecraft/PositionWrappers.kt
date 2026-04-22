package com.wolfyscript.scafall.wrappers.minecraft

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.ScafallBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallBlockPosCommon
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPosCommon
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalPrecisePos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalPrecisePosCommon
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePosCommon
import net.minecraft.core.BlockPos
import net.minecraft.core.GlobalPos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

fun GlobalPos.wrap(): ScafallGlobalBlockPos {
    val dimension = dimension().identifier()
    return ScafallGlobalBlockPosCommon(Key.key(dimension.namespace, dimension.path), pos.wrap())
}

fun BlockPos.wrap(): ScafallBlockPos = ScafallBlockPosCommon(this)

fun BlockPos.wrap(dimension: Key): ScafallGlobalBlockPos = ScafallGlobalBlockPosCommon(dimension, this.wrap())

fun Vec3.wrap() = ScafallPrecisePosCommon(this)

fun Vec3.wrap(dimension: Key) = ScafallGlobalPrecisePosCommon(dimension, this.wrap())

// unwrap

fun ScafallGlobalBlockPos.unwrap(): GlobalPos {
    return GlobalPos.of(
        ResourceKey.create(
            Registries.DIMENSION,
            Identifier.fromNamespaceAndPath(
                dimension.namespace,
                dimension.value
            )
        ), blockPos.unwrap()
    )
}

fun ScafallGlobalPrecisePos.unwrap(): Pair<Vec3, ResourceKey<Level>> {
    return Pair(
        pos.unwrap(), ResourceKey.create(
            Registries.DIMENSION,
            Identifier.fromNamespaceAndPath(
                dimension.namespace,
                dimension.value
            )
        )
    )
}

fun ScafallBlockPos.unwrap(): BlockPos = BlockPos(x, y, z)

fun ScafallPrecisePos.unwrap(): Vec3 = Vec3(x, y, z)
