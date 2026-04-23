package com.wolfyscript.scafall.wrappers.minecraft

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.wrappers.ScafallPlayer
import com.wolfyscript.scafall.wrappers.world.entity.ScafallPlayerCommon
import net.minecraft.world.entity.player.Player

/**
 * Wraps this [net.minecraft.server.level.ServerPlayer] or [net.minecraft.client.player.AbstractClientPlayer].
 *
 * The specific type is lost and should be checked after unwrapping!
 */
fun Player.wrap(): ScafallPlayer = ScafallPlayerCommon(this)

/**
 * Unwraps the Player to the Minecraft Player.
 * @return The minecraft Player; null if the player is no longer available.
 */
fun ScafallPlayer.unwrap(): Player? = ScafallProvider.get().server?.minecraftServer?.playerList?.getPlayer(uuid)