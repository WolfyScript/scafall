package com.wolfyscript.scafall.wrappers.world.sound

import com.wolfyscript.scafall.identifier.Key

data class SoundEvent(
    val sound: Key,
    val range: Float? = null,
)
