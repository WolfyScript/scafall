package com.wolfyscript.scafall.wrappers.world.entity

import net.kyori.adventure.text.Component

interface Player : Entity {
    val displayName: Component?
}
