package com.wolfyscript.scafall.wrappers.world.entity

import com.wolfyscript.scafall.wrappers.ScafallPlayer
import com.wolfyscript.scafall.wrappers.ScafallServerPlayer
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import com.wolfyscript.scafall.wrappers.minecraft.wrap
import net.kyori.adventure.text.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import java.lang.ref.WeakReference
import java.util.UUID

class ScafallPlayerCommon(val player: Player) : ScafallPlayer {

    override val uuid: UUID = player.uuid

    override val pos: ScafallPrecisePos
        get() {
            return player.position().wrap()
        }

}

class ScafallServerPlayerImpl(val serverPlayer: ServerPlayer) : ScafallServerPlayer {

    override val uuid: UUID
        get() = serverPlayer.uuid
    override val pos: ScafallPrecisePos
        get() = serverPlayer.position().wrap()

}