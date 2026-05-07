package com.wolfyscript.scafall.wrappers.world

import com.wolfyscript.scafall.identifier.Key

/**
 * Represents a position that is bound to a world
 */
interface ScafallGlobalBlockPos {

    /**
     * The dimension key where this global block position is located.
     */
    val dimension: Key

    /**
     * The block position within the dimension.
     */
    val blockPos: ScafallBlockPos

}

/**
 * Represents a precise position that is bound to a world
 */
interface ScafallGlobalPrecisePos {

    /**
     * The dimension key where this global precise position is located.
     */
    val dimension: Key

    /**
     * The precise position within the dimension.
     */
    val pos: ScafallPrecisePos

}

/**
 * Represents a block position with integer coordinates
 */
interface ScafallBlockPos {

    /**
     * The x-coordinate of the block position.
     */
    val x: Int

    /**
     * The y-coordinate of the block position.
     */
    val y: Int

    /**
     * The z-coordinate of the block position.
     */
    val z: Int

}

/**
 * Represents a precise position with double coordinates
 */
interface ScafallPrecisePos {

    /**
     * The x-coordinate of the precise position.
     */
    val x: Double

    /**
     * The y-coordinate of the precise position.
     */
    val y: Double

    /**
     * The z-coordinate of the precise position.
     */
    val z: Double

}

