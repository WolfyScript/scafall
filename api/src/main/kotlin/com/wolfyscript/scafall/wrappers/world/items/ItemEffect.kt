package com.wolfyscript.scafall.wrappers.world.items

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent

interface ItemEffect {

    data class ApplyEffects(
        val effects: List<Effect>,
        val probability: Float,
    ) : ItemEffect {

        data class Effect(
            val id: Key,
            val amplifier: Byte,
            val duration: Int,
            val ambient: Boolean = false,
            val showParticles: Boolean = true,
            val showIcon: Boolean = true,
        )

    }

    data class RemoveEffects(
        val effects: List<Key>,
    ) : ItemEffect

    class ClearAllEffects : ItemEffect

    data class TeleportRandomly(val diameter: Float) : ItemEffect

    data class PlaySound(
        val soundEvent: SoundEvent
    ) : ItemEffect

}