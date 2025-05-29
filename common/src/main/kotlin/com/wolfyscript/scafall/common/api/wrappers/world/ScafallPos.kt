package com.wolfyscript.scafall.common.api.wrappers.world

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.ScafallBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalPrecisePos
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3

class ScafallBlockPosCommon(val blockPos: BlockPos) : ScafallBlockPos {

    override val x: Int = blockPos.x
    override val y: Int = blockPos.y
    override val z: Int = blockPos.z

}

class ScafallPrecisePosCommon(val pos: Vec3) : ScafallPrecisePos {

    override val x: Double = pos.x
    override val y: Double = pos.y
    override val z: Double = pos.z

}

class ScafallGlobalBlockPosCommon(
    override val dimension: Key,
    override val blockPos: ScafallBlockPos,
) : ScafallGlobalBlockPos

class ScafallGlobalPrecisePosCommon(
    override val dimension: Key,
    override val pos: ScafallPrecisePos,
) : ScafallGlobalPrecisePos