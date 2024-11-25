package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent

data class Equippable(
    val slot: String,
    val equipSound: SoundEvent,
    val assetId: Key?,
    val allowedEntities: List<Key>?,
    val dispensable: Boolean = true,
    val swappable: Boolean = true,
    val damageOnHurt: Boolean = true,
    val cameraOverlay: Key? = null,
)