package com.wolfyscript.scafall.common.api.wrappers.world.entity

import com.wolfyscript.scafall.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import com.wolfyscript.scafall.wrappers.world.entity.Player
import net.kyori.adventure.text.Component
import java.lang.ref.WeakReference
import java.util.UUID

class PlayerCommon(player: net.minecraft.world.entity.player.Player) : Player {

    private val ref = WeakReference(player)

    override val uuid: UUID = player.uuid

    override var displayName: Component? = null //TODO

    override val pos: ScafallPrecisePos
        get() {
            return ref.get()?.position()?.wrap() ?: throw IllegalStateException("Player is not available!")
        }

}