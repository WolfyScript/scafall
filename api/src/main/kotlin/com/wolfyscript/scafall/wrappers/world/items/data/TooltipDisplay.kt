package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key

interface TooltipDisplay {

    var displayed: Boolean

    val hiddenComponents: MutableSet<Key>

}