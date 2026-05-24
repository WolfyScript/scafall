package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.wrappers.minecraft.unwrap
import com.wolfyscript.scafall.wrappers.world.entity.ScafallPlayer
import com.wolfyscript.scafall.wrappers.world.entity.ScafallServerPlayer
import com.wolfyscript.scafall.wrappers.minecraft.wrap
import net.minecraft.server.level.ServerPlayer
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

fun Player.into(): ServerPlayer {
    return (this as CraftPlayer).handle
}

fun net.minecraft.world.entity.player.Player.into(): Player {
    require(this is ServerPlayer) { "Player is not a ServerPlayer" }
    return this.bukkitEntity as Player
}

/**
 * Wraps this [Player] in a scafall wrapper.
 */
fun Player.wrap(): ScafallServerPlayer {
    return into().wrap()
}

/**
 * Unwraps the [ScafallServerPlayer] to a Spigot/Bukkit [Player].
 *
 * In case the wrapper is not a [ScafallServerPlayer], it tries to find a player using the uuid.
 */
fun ScafallPlayer.unwrapSpigot(): Player? {
    if (this is ScafallServerPlayer) {
        return this.unwrapSpigot()
    }
    throw IllegalArgumentException("Client Players are currently not supported")
}

/**
 * Unwraps the [ScafallServerPlayer] to a Spigot/Bukkit [Player].
 */
fun ScafallServerPlayer.unwrapSpigot(): Player {
    return this.unwrap().into()
}
