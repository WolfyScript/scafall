package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key

interface TooltipDisplay {
    val hideTooltips: Boolean
    val hiddenComponents: MutableSet<Key>
}