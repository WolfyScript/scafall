package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.wrappers.world.entity.ScafallPlayer
import com.wolfyscript.scafall.wrappers.world.entity.ScafallServerPlayer
import com.wolfyscript.scafall.wrappers.minecraft.wrap
import com.wolfyscript.scafall.wrappers.world.entity.ScafallServerPlayerImpl
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player


/**
 * Wraps this [Player] in a scafall wrapper.
 */
fun Player.wrap(): ScafallServerPlayer {
    return (player as CraftPlayer).handle.wrap()
}

/**
 * Unwraps the [ScafallServerPlayer] to a Spigot/Bukkit [Player].
 *
 * In case the wrapper is not a [ScafallServerPlayer], it tries to find a player using the uuid.
 */
fun ScafallPlayer.unwrapSpigot(): Player? {
    if (this is ScafallServerPlayer) {
        return (this as ScafallServerPlayer).unwrapSpigot()
    }
    return Bukkit.getPlayer(uuid)
}

/**
 * Unwraps the [ScafallServerPlayer] to a Spigot/Bukkit [Player].
 */
fun ScafallServerPlayer.unwrapSpigot(): Player {
    return (this as ScafallServerPlayerImpl).serverPlayer.bukkitEntity
}
