package com.wolfyscript.scafall.sponge.api.wrappers.world.entity

import net.kyori.adventure.text.Component
import org.spongepowered.api.entity.living.player.Player

class SpongePlayer(ref: Player) : SpongeEntity<Player>(ref), com.wolfyscript.scafall.wrappers.world.entity.Player {

    override var displayName: Component?
        get() = ref.displayName().get()
        set(value) {}

}