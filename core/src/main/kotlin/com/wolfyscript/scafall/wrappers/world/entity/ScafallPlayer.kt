package com.wolfyscript.scafall.wrappers.world.entity

import com.wolfyscript.scafall.wrappers.minecraft.wrap
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import java.util.UUID

/**
 * Wrapper for any type of Minecraft Player.
 *
 * The specific type is not available, so it may wrap a ServerPlayer or ClientPlayer.
 */
interface ScafallPlayer : Entity

/**
 * Wrapper for a Minecraft ServerPlayer
 */
interface ScafallServerPlayer : Entity

internal class ScafallPlayerCommon(val player: Player) : ScafallPlayer {

    override val uuid: UUID = player.uuid

    override val pos: ScafallPrecisePos
        get() {
            return player.position().wrap()
        }

}

internal class ScafallServerPlayerImpl(val serverPlayer: ServerPlayer) : ScafallServerPlayer {

    override val uuid: UUID
        get() = serverPlayer.uuid
    override val pos: ScafallPrecisePos
        get() = serverPlayer.position().wrap()

}
