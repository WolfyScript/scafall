package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.PotionContentsImpl
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.ItemEffect
import com.wolfyscript.scafall.wrappers.world.items.data.*
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.FoodProperties
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation
import io.papermc.paper.potion.SuspiciousEffectEntry
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.set.RegistrySet
import org.bukkit.inventory.meta.components.FoodComponent
import org.bukkit.potion.PotionEffect

private fun ConsumeEffect.convert(): ItemEffect {
    return when (this) {
        is ConsumeEffect.ApplyStatusEffects -> {
            ItemEffect.ApplyEffects(
                effects().map {
                    ItemEffect.ApplyEffects.Effect(
                        it.type.key.api(),
                        it.amplifier.toByte(),
                        it.duration,
                        it.isAmbient,
                        it.hasParticles(),
                        it.hasIcon()
                    )
                },
                probability()
            )
        }

        is ConsumeEffect.RemoveStatusEffects -> {
            ItemEffect.RemoveEffects(
                removeEffects().map { it.toAPI() }
            )
        }

        is ConsumeEffect.ClearAllStatusEffects -> {
            ItemEffect.ClearAllEffects()
        }

        is ConsumeEffect.TeleportRandomly -> {
            ItemEffect.TeleportRandomly(
                diameter()
            )
        }

        is ConsumeEffect.PlaySound -> {
            ItemEffect.PlaySound(
                SoundEvent(
                    sound().toAPI()
                )
            )
        }

        else -> {
            throw NotImplementedError("Effect type ${this.javaClass.typeName} is not implemented!")
        }
    }
}

private fun ItemEffect.convert(): ConsumeEffect {
    return when (this) {
        is ItemEffect.ApplyEffects -> {
            ConsumeEffect.applyStatusEffects(
                effects.map {
                    PotionEffect(
                        RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(it.id.into()),
                        it.duration,
                        it.amplifier.toInt(),
                        it.ambient,
                        it.showParticles,
                        it.showIcon
                    )
                },
                probability
            )
        }

        is ItemEffect.RemoveEffects -> {
            ConsumeEffect.removeEffects(
                RegistrySet.keySet(
                    RegistryKey.MOB_EFFECT,
                    effects.map { TypedKey.create(RegistryKey.MOB_EFFECT, it.into()) })
            )
        }

        is ItemEffect.ClearAllEffects -> {
            ConsumeEffect.clearAllStatusEffects()
        }

        is ItemEffect.TeleportRandomly -> {
            ConsumeEffect.teleportRandomlyEffect(diameter)
        }

        is ItemEffect.PlaySound -> {
            ConsumeEffect.playSoundConsumeEffect(soundEvent.sound.into())
        }

        else -> {
            throw NotImplementedError("Effect type ${this.javaClass.typeName} is not implemented!")
        }
    }


}

internal val consumableConverter = PaperDataAPIConverter(
    {
        val consumable = unwrap().getData(DataComponentTypes.CONSUMABLE)
        if (consumable == null) {
            return@PaperDataAPIConverter Result.success(null)
        }

        Result.success(
            Consumable(
                consumable.consumeSeconds(),
                Key.minecraft(consumable.animation().toString().lowercase()),
                SoundEvent(consumable.sound().toAPI()),
                consumable.hasConsumeParticles(),
                consumable.consumeEffects().map { it.convert() }
            ))
    }, {
        unwrap().setData(
            DataComponentTypes.CONSUMABLE,
            io.papermc.paper.datacomponent.item.Consumable.consumable()
                .consumeSeconds(it.consumeSeconds)
                .animation(ItemUseAnimation.valueOf(it.animation.value.uppercase()))
                .sound(it.sound.sound.into())
                .hasConsumeParticles(it.consumeParticles)
                .addEffects(it.onConsumeEffects.map { it.convert() })
        )
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.CONSUMABLE)
        Result.success(this to true)
    }
)

internal val deathProtectionConverter = PaperDataAPIConverter(
    {
        val deathProtection = unwrap().getData(DataComponentTypes.DEATH_PROTECTION)
        if (deathProtection == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        return@PaperDataAPIConverter Result.success(
            DeathProtection(
                deathProtection!!.deathEffects().map { it.convert() })
        )
    }, {
        unwrap().setData(
            DataComponentTypes.DEATH_PROTECTION,
            io.papermc.paper.datacomponent.item.DeathProtection.deathProtection(it.deathEffects.map { it.convert() })
        )
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.DEATH_PROTECTION)
        Result.success(this to true)
    }
)

internal val ominousBottleAmplifierConverter = PaperDataAPIConverter(
    {
        Result.success(
            unwrap().getData(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER)
                ?.let { OminousBottleAmplifier(it.amplifier()) })
    }, {
        unwrap().setData(
            DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER,
            io.papermc.paper.datacomponent.item.OminousBottleAmplifier.amplifier(it.amplifier)
        )
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.OMINOUS_BOTTLE_AMPLIFIER)
        Result.success(this to true)
    }
)

internal val suspiciousStewEffectsConverter = PaperDataAPIConverter(
    {
        val effects = unwrap().getData(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS)
        if (effects == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        Result.success(
            SuspiciousStewEffects(
                effects.effects().map { SuspiciousStewEffects.EffectEntry(it.effect().key.api(), it.duration()) })
        )
    }, {
        unwrap().setData(
            DataComponentTypes.SUSPICIOUS_STEW_EFFECTS,
            io.papermc.paper.datacomponent.item.SuspiciousStewEffects.suspiciousStewEffects(it.effects.map {
                SuspiciousEffectEntry.create(
                    RegistryAccess.registryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(it.effectType.into()),
                    it.duration
                )
            })
        )
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS)
        Result.success(this to true)
    }
)

internal val foodConverter = PaperDataAPIConverter(
    {
        val food = unwrap().getData(DataComponentTypes.FOOD)
        if (food == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        Result.success(
            Food(
                food.nutrition(),
                food.saturation(),
                food.canAlwaysEat(),
            )
        )
    }, {
        unwrap().setData(DataComponentTypes.FOOD, FoodProperties.food().nutrition(it.nutrition).saturation(it.saturation).canAlwaysEat(it.canAlwaysEat))
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.FOOD)
        Result.success(this to true)
    }
)

internal val potionContentsConverter = PaperDataAPIConverter<PotionContents>(
    {
        val potionContents = unwrap().getData(DataComponentTypes.POTION_CONTENTS)
        if (potionContents == null) {
            return@PaperDataAPIConverter Result.success(null)
        }

        Result.success(
            PotionContentsImpl()
        )
    }, {
        Result.failure(NotImplementedError())
    }, {
        unwrap().unsetData(DataComponentTypes.POTION_CONTENTS)
        Result.success(this to true)
    }
)