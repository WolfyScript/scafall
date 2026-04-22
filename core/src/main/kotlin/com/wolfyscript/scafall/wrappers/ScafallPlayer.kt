package com.wolfyscript.scafall.wrappers

import com.wolfyscript.scafall.wrappers.world.entity.Entity
import net.kyori.adventure.text.Component

interface ScafallPlayer : Entity {
    val displayName: Component?
}