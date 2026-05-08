package com.wolfyscript.scafall.wrappers.world.entity

import com.wolfyscript.scafall.wrappers.minecraft.wrap
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import net.minecraft.server.level.ServerPlayer
import java.util.UUID

internal class ScafallServerPlayerImpl(val serverPlayer: ServerPlayer) : ScafallServerPlayer {

    override val uuid: UUID
        get() = serverPlayer.uuid
    override val pos: ScafallPrecisePos
        get() = serverPlayer.position().wrap()

}