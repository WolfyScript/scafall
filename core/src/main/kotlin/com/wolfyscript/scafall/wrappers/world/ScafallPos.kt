package com.wolfyscript.scafall.wrappers.world

import com.wolfyscript.scafall.identifier.Key

/**
 * Represents a position that is bound to a world
 */
interface ScafallGlobalBlockPos {

    val dimension: Key

    val blockPos: ScafallBlockPos

}

interface ScafallGlobalPrecisePos {

    val dimension: Key

    val pos: ScafallPrecisePos

}

interface ScafallBlockPos {

    val x: Int
    val y: Int
    val z: Int

}

interface ScafallPrecisePos {

    val x: Double
    val y: Double
    val z: Double
}

