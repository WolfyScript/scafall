package com.wolfyscript.scafall.spigot.api.wrappers.world

import com.wolfyscript.scafall.spigot.api.wrappers.BukkitRefAdapter
import com.wolfyscript.scafall.wrappers.world.Block
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPos
import org.bukkit.World

class BukkitWorld(world: World) : BukkitRefAdapter<World>(world), com.wolfyscript.scafall.wrappers.world.World {

    override fun getBlockAt(x: Int, y: Int, z: Int): Block {
        TODO("Not yet implemented")
    }

    override fun getBlockAt(location: ScafallGlobalBlockPos?): Block? {
        TODO("Not yet implemented")
    }
}