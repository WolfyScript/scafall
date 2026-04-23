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

/**
 * Wraps this [GlobalPos] in a [ScafallGlobalBlockPos] that holds a reference to the dimension and position.
 */
fun GlobalPos.wrap(): ScafallGlobalBlockPos {
    val dimension = dimension().identifier()
    return ScafallGlobalBlockPosCommon(Key.key(dimension.namespace, dimension.path), pos.wrap())
}

/**
 * Wraps this [BlockPos] in a [ScafallBlockPos]
 */
fun BlockPos.wrap(): ScafallBlockPos = ScafallBlockPosCommon(this)

/**
 * Wraps this [BlockPos] and a specified [dimension] in a [ScafallGlobalBlockPos]
 */
fun BlockPos.wrap(dimension: Key): ScafallGlobalBlockPos = ScafallGlobalBlockPosCommon(dimension, this.wrap())

/**
 * Wraps this [Vec3] in a [ScafallPrecisePos]
 */
fun Vec3.wrap() = ScafallPrecisePosCommon(this)

/**
 * Wraps this [Vec3] and a specified [dimension] in a [ScafallGlobalPrecisePos]
 */
fun Vec3.wrap(dimension: Key) = ScafallGlobalPrecisePosCommon(dimension, this.wrap())

/**
 * Unwraps this to the Minecraft [GlobalPos]
 */
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

/**
 * Unwraps this to a [Vec3] and dimension [ResourceKey].
 *
 * There is no exact representation for this in Minecraft, so it is split and returned as a [Pair]
 */
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

/**
 * Unwraps this to the Minecraft [BlockPos]
 */
fun ScafallBlockPos.unwrap(): BlockPos = BlockPos(x, y, z)

/**
 * Unwraps this precise position to a Minecraft [Vec3]
 */
fun ScafallPrecisePos.unwrap(): Vec3 = Vec3(x, y, z)
