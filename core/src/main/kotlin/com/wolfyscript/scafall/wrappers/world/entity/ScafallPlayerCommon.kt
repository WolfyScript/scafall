package com.wolfyscript.scafall.wrappers.world.entity

import com.wolfyscript.scafall.wrappers.minecraft.wrap
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import net.minecraft.world.entity.player.Player
import java.util.UUID

internal class ScafallPlayerCommon(val player: Player) : ScafallPlayer {

    override val uuid: UUID = player.uuid

    override val pos: ScafallPrecisePos
        get() {
            return player.position().wrap()
        }

}