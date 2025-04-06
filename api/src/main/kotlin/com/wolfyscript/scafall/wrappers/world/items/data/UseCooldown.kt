package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key

interface UseCooldown {

    var seconds: Float

    var cooldownGroup: Key?

}