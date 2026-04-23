package com.wolfyscript.scafall.wrappers.world.entity

import com.wolfyscript.scafall.wrappers.ScafallPlayer
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import com.wolfyscript.scafall.wrappers.minecraft.wrap
import net.kyori.adventure.text.Component
import net.minecraft.world.entity.player.Player
import java.lang.ref.WeakReference
import java.util.UUID

class ScafallPlayerCommon(player: Player) : ScafallPlayer {

    private val ref = WeakReference(player)

    override val uuid: UUID = player.uuid

    override val pos: ScafallPrecisePos
        get() {
            return ref.get()?.position()?.wrap() ?: throw IllegalStateException("Player is not available!")
        }

}