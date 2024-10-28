package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.data.DyedColor
import com.wolfyscript.scafall.wrappers.world.items.data.FireworkExplosion
import com.wolfyscript.scafall.wrappers.world.items.data.Fireworks

internal val fireworksItemMetaConverter =
    ItemMetaDataKeyConverter<Fireworks>({ TODO("Not yet implemented") }, { TODO("Not yet implemented") })

internal val fireworkExplosionItemMetaConverter =
    ItemMetaDataKeyConverter<FireworkExplosion>({ TODO("Not yet implemented") }, { TODO("Not yet implemented") })

data class FireworksImpl(
    override val explosions: List<FireworkExplosion>,
    override val flightDuration: Byte
) : Fireworks

data class FireworkExplosionImpl(
    override val shape: Key,
    override val colors: List<DyedColor>,
    override val fadeColors: List<DyedColor>,
    override val trail: Boolean,
    override val twinkle: Boolean
) : FireworkExplosion