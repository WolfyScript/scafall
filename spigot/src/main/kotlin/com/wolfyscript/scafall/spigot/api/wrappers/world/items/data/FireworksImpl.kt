package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.Color
import com.wolfyscript.scafall.wrappers.world.items.data.FireworkExplosion
import com.wolfyscript.scafall.wrappers.world.items.data.Fireworks
import org.bukkit.FireworkEffect
import org.bukkit.inventory.meta.FireworkEffectMeta
import org.bukkit.inventory.meta.FireworkMeta

internal val fireworksItemMetaConverter =
    ItemMetaDataKeyConverter<Fireworks>({
        if (this is FireworkMeta) {
            return@ItemMetaDataKeyConverter FireworksImpl(
                effects.map { effect ->
                    FireworkExplosionImpl(
                        shape = Key.key(
                            Key.MINECRAFT_NAMESPACE, when (effect.type) {
                                FireworkEffect.Type.BALL -> "ball"
                                FireworkEffect.Type.BALL_LARGE -> "large_ball"
                                FireworkEffect.Type.STAR -> "star"
                                FireworkEffect.Type.BURST -> "burst"
                                FireworkEffect.Type.CREEPER -> "creeper"
                            }
                        ),
                        colors = effect.colors.map { it.wrap() },
                        fadeColors = emptyList(),
                        trail = effect.hasTrail(),
                        twinkle = effect.hasFlicker()
                    )
                },
                flightDuration = this.power
            )
        }

        null
    }, {
        if (this is FireworkMeta) {
            clearEffects() // Replace the explosions
            addEffects(
                it.explosions.map { effect ->
                    FireworkEffect.builder()
                        .trail(effect.trail)
                        .flicker(effect.twinkle)
                        .withColor(effect.colors.map { it.unwrap() })
                        .withFade(effect.fadeColors.map { it.unwrap() })
                        .build()
                }
            )
            power = it.flightDuration
        }
    })

internal val fireworkExplosionItemMetaConverter =
    ItemMetaDataKeyConverter<FireworkExplosion>({
        if (this is FireworkEffectMeta && hasEffect()) {
            return@ItemMetaDataKeyConverter effect?.let { effect ->
                FireworkExplosionImpl(
                    shape = Key.key(
                        Key.MINECRAFT_NAMESPACE, when (effect.type) {
                            FireworkEffect.Type.BALL -> "ball"
                            FireworkEffect.Type.BALL_LARGE -> "large_ball"
                            FireworkEffect.Type.STAR -> "star"
                            FireworkEffect.Type.BURST -> "burst"
                            FireworkEffect.Type.CREEPER -> "creeper"
                        }
                    ),
                    colors = effect.colors.map { it.wrap() },
                    fadeColors = effect.fadeColors.map { it.wrap() },
                    trail = effect.hasTrail(),
                    twinkle = effect.hasFlicker()
                )
            }
        }
        null
    }, {
        if (this is FireworkEffectMeta) {
            effect = FireworkEffect.builder()
                .trail(it.trail)
                .flicker(it.twinkle)
                .withColor(it.colors.map { it.unwrap() })
                .withFade(it.fadeColors.map { it.unwrap() })
                .build()
        }
    })

data class FireworksImpl(
    override val explosions: List<FireworkExplosion>,
    override val flightDuration: Int,
) : Fireworks

data class FireworkExplosionImpl(
    override val shape: Key,
    override val colors: List<Color>,
    override val fadeColors: List<Color>,
    override val trail: Boolean,
    override val twinkle: Boolean,
) : FireworkExplosion