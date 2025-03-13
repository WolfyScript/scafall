package com.wolfyscript.scafall.common.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.data.Trim

data class TrimImpl(override val showInTooltip: Boolean,
                    override val pattern: Key,
                    override val material: Key
) : Trim
