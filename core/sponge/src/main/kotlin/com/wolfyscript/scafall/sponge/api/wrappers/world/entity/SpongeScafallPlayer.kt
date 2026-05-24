package com.wolfyscript.scafall.sponge.api.wrappers.world.entity

import com.wolfyscript.scafall.wrappers.ScafallPlayer
import net.kyori.adventure.text.Component
import org.spongepowered.api.entity.living.player.Player

class SpongeScafallPlayer(ref: Player) : SpongeEntity<Player>(ref), ScafallPlayer {

    override val displayName: Component?
        get() = ref.get()?.displayName()?.get()

}