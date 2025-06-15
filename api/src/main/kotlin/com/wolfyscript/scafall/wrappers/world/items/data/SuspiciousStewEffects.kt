package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.ItemEffect

data class SuspiciousStewEffects(val effects: List<EffectEntry>) {

    data class EffectEntry(val effectType: Key, val duration: Int)
}
