package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.wrappers.minecraft.wrap
import com.wolfyscript.scafall.wrappers.world.ScafallBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalPrecisePos
import net.minecraft.world.phys.Vec3
import org.bukkit.Location
import org.bukkit.craftbukkit.util.CraftLocation

/**
 * Wraps this [Location] in a [ScafallGlobalPrecisePos] (PrecisePos with an associated Level)
 *
 * @return The global precise position; or null when [Location.world] is not available
 */
fun Location.toPreciseGlobal(): ScafallGlobalPrecisePos? {
    if (world == null) {
        return null
    }
    return Vec3(x, y, z).wrap(world.key.toScafall())
}

/**
 * Wraps this [Location] in a [com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos]
 */
fun Location.toPrecise(): com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos {
    return Vec3(x, y, z).wrap()
}

/**
 * Wraps this [Location] in a [com.wolfyscript.scafall.wrappers.world.ScafallBlockPos]
 */
fun Location.toBlockPos(): ScafallBlockPos {
    return CraftLocation.toBlockPos(this).wrap()
}

/**
 * Wraps this [Location] in a [ScafallGlobalBlockPos] (BlockPos with an associated Level)
 *
 * @return The global block position; or null when [Location.world] is not available
 */
fun Location.toBlockPosGlobal(): ScafallGlobalBlockPos? {
    if (world == null) {
        return null
    }
    return CraftLocation.toBlockPos(this).wrap(world.key.toScafall())
}
