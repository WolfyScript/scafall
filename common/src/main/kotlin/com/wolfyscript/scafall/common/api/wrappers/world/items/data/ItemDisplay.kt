package com.wolfyscript.scafall.common.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.data.TooltipDisplay

data class TooltipDisplayCommon(
    override var displayed: Boolean,
    override val hiddenComponents: MutableSet<Key>
) : TooltipDisplay