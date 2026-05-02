package com.wolfyscript.scafall.wrappers.minecraft

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.wrappers.ScafallPlayer
import com.wolfyscript.scafall.wrappers.ScafallServerPlayer
import com.wolfyscript.scafall.wrappers.world.entity.ScafallPlayerCommon
import com.wolfyscript.scafall.wrappers.world.entity.ScafallServerPlayerImpl
import net.minecraft.server.level.ServerPlayer
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

/**
 * Wraps this [ServerPlayer] to a type specific player wrapper.
 */
fun ServerPlayer.wrap(): ScafallServerPlayer = ScafallServerPlayerImpl(this)

/**
 * Unwraps this [ScafallServerPlayer] to the Minecraft ServerPlayer.
 *
 * @return the unwrapped Minecraft ServerPlayer. null if the player is no longer available.
 */
fun ScafallServerPlayer.unwrap(): ServerPlayer = (this as ScafallServerPlayerImpl).serverPlayer