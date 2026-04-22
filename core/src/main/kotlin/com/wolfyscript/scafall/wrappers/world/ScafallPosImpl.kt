package com.wolfyscript.scafall.wrappers.world

import com.wolfyscript.scafall.identifier.Key
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3

class ScafallBlockPosCommon(val blockPos: BlockPos) : ScafallBlockPos {

    override val x: Int = blockPos.x
    override val y: Int = blockPos.y
    override val z: Int = blockPos.z

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScafallBlockPosCommon) return false
        if (blockPos != other.blockPos) return false
        return true
    }

    override fun hashCode(): Int {
        return blockPos.hashCode()
    }

}

class ScafallPrecisePosCommon(val pos: Vec3) : ScafallPrecisePos {

    override val x: Double = pos.x
    override val y: Double = pos.y
    override val z: Double = pos.z

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScafallPrecisePosCommon) return false

        if (pos != other.pos) return false

        return true
    }

    override fun hashCode(): Int {
        return pos.hashCode()
    }

}

class ScafallGlobalBlockPosCommon(
    override val dimension: Key,
    override val blockPos: ScafallBlockPos,
) : ScafallGlobalBlockPos {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScafallGlobalBlockPosCommon) return false

        if (dimension != other.dimension) return false
        if (blockPos != other.blockPos) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dimension.hashCode()
        result = 31 * result + blockPos.hashCode()
        return result
    }
}

class ScafallGlobalPrecisePosCommon(
    override val dimension: Key,
    override val pos: ScafallPrecisePos,
) : ScafallGlobalPrecisePos {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScafallGlobalPrecisePosCommon) return false

        if (dimension != other.dimension) return false
        if (pos != other.pos) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dimension.hashCode()
        result = 31 * result + pos.hashCode()
        return result
    }
}