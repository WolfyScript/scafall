package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.Color

interface Fireworks {

    val explosions: List<FireworkExplosion>
    val flightDuration: Int

}

interface FireworkExplosion {

    val shape: Key
    val colors: List<Color>
    val fadeColors: List<Color>
    val trail: Boolean
    val twinkle: Boolean

}
