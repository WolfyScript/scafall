package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.ItemEffect
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent

data class Consumable(
    val consumeSeconds: Float,
    val animation: Key,
    val sound: SoundEvent,
    val consumeParticles: Boolean = true,
    val onConsumeEffects: List<ItemEffect>
)
