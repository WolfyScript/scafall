package com.wolfyscript.scafall.wrappers.world

interface World {
    fun getBlockAt(x: Int, y: Int, z: Int): Block

    fun getBlockAt(location: ScafallGlobalBlockPos?): Block?
}
