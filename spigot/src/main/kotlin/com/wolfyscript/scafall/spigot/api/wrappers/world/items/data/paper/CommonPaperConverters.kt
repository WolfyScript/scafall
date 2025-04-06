package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.common.api.wrappers.world.items.data.TrimImpl
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.identifiers.api
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.toBukkit
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.toWrapper
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.DyeColor
import com.wolfyscript.scafall.wrappers.world.items.data.*
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemArmorTrim
import io.papermc.paper.datacomponent.item.PotDecorations
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.set.RegistrySet
import io.papermc.paper.registry.tag.TagKey
import org.bukkit.Registry
import org.bukkit.inventory.ItemRarity
import org.bukkit.inventory.meta.trim.ArmorTrim

internal val baseColorConverter = PaperDataAPIConverter<DyeColor>(
    {
        Result.success(unwrap().getData(DataComponentTypes.BASE_COLOR)?.toWrapper())
    }, {
        unwrap().setData(DataComponentTypes.BASE_COLOR, it.toBukkit())
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.BASE_COLOR)
        Result.success(this to true)
    }
)

internal val unbreakableConverter = PaperDataAPIConverter<Unbreakable>(
    {
        if (unwrap().hasData(DataComponentTypes.UNBREAKABLE)) {
            Result.success(Unbreakable(false))
        }
        Result.success(null)
    }, {
        unwrap().setData(DataComponentTypes.UNBREAKABLE)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.UNBREAKABLE)
        Result.success(this to true)
    }
)

internal val damageConverter = PaperDataAPIConverter<Int>(
    {
        Result.success(unwrap().getData(DataComponentTypes.DAMAGE))
    }, {
        unwrap().setData(DataComponentTypes.DAMAGE, it)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.DAMAGE)
        Result.success(this to true)
    }
)

internal val damageResistantConverter = PaperDataAPIConverter(
    {
        Result.success(
            unwrap().getData(DataComponentTypes.DAMAGE_RESISTANT)
                ?.let { DamageResistant(listOf(it.types().key().toAPI())) })
    }, {
        unwrap().setData(
            DataComponentTypes.DAMAGE_RESISTANT,
            io.papermc.paper.datacomponent.item.DamageResistant.damageResistant(
                TagKey.create(
                    RegistryKey.DAMAGE_TYPE,
                    it.types[0].into()
                )
            )
        )
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.DAMAGE_RESISTANT)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)

internal val maxDamageConverter = PaperDataAPIConverter<Int>(
    {
        Result.success(unwrap().getData(DataComponentTypes.MAX_DAMAGE))
    }, {
        unwrap().setData(DataComponentTypes.MAX_DAMAGE, it)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.MAX_DAMAGE)
        Result.success(this to true)
    }
)

internal val maxStackSizeConverter = PaperDataAPIConverter<Int>(
    {
        Result.success(unwrap().getData(DataComponentTypes.MAX_STACK_SIZE))
    }, {
        unwrap().setData(DataComponentTypes.MAX_STACK_SIZE, it)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.MAX_STACK_SIZE)
        Result.success(this to true)
    }
)

internal val repairCostConverter = PaperDataAPIConverter<Int>(
    {
        Result.success(unwrap().getData(DataComponentTypes.REPAIR_COST))
    }, {
        unwrap().setData(DataComponentTypes.REPAIR_COST, it)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.REPAIR_COST)
        Result.success(this to true)
    }
)

internal val repairableConverter = PaperDataAPIConverter<Repairable>(
    {
        val repairable = unwrap().getData(DataComponentTypes.REPAIRABLE)
        if (repairable == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        Result.success(Repairable(repairable.types().map { it.toAPI() }))
    }, {
        unwrap().setData(
            DataComponentTypes.REPAIRABLE, io.papermc.paper.datacomponent.item.Repairable.repairable(
                RegistrySet.keySet(RegistryKey.ITEM, it.types.map { TypedKey.create(RegistryKey.ITEM, it.into()) })
            )
        )
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.REPAIRABLE)
        Result.success(this to true)
    }
)

internal val instrumentConverter = PaperDataAPIConverter(
    {
        Result.success(unwrap().getData(DataComponentTypes.INSTRUMENT)?.key?.api())
    }, {
        val instrument = Registry.INSTRUMENT.get(it.bukkit())
            ?: return@PaperDataAPIConverter Result.failure(Exception("Failed to set Instrument: Could not find instrument of type $it!"))
        unwrap().setData(DataComponentTypes.INSTRUMENT, instrument)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.INSTRUMENT)
        Result.success(this to true)
    }
)

internal val recipesConverter = PaperDataAPIConverter(
    {
        val recipes = unwrap().getData(DataComponentTypes.RECIPES) ?: return@PaperDataAPIConverter Result.success(null)
        Result.success(recipes.map { it.toAPI() }.toList())
    }, { list ->
        unwrap().setData(DataComponentTypes.RECIPES, list.map { it.bukkit() })
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.RECIPES)
        Result.success(this to true)
    }
)

internal val noteBlockSoundConverter = PaperDataAPIConverter(
    {
        val sound = unwrap().getData(DataComponentTypes.NOTE_BLOCK_SOUND)
        Result.success(sound?.toAPI())
    }, {
        unwrap().setData(DataComponentTypes.NOTE_BLOCK_SOUND, it.bukkit())
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.NOTE_BLOCK_SOUND)
        Result.success(this to true)
    }
)

internal val potDecorationsConverter = PaperDataAPIConverter(
    {
        val decorations =
            unwrap().getData(DataComponentTypes.POT_DECORATIONS) ?: return@PaperDataAPIConverter Result.success(null)
        // north, west, east, south // when placed facing north
        // The data component internally just uses a list of sherds
        return@PaperDataAPIConverter Result.success(buildList<Key> {
            // Assumed based on facing direction when placed down
            add(decorations.front()?.key?.toAPI() ?: Key.minecraft("brick"))
            add(decorations.right()?.key?.toAPI() ?: Key.minecraft("brick"))
            add(decorations.left()?.key?.toAPI() ?: Key.minecraft("brick"))
            add(decorations.back()?.key?.toAPI() ?: Key.minecraft("brick"))
        })
    }, {
        unwrap().setData(
            DataComponentTypes.POT_DECORATIONS, PotDecorations.potDecorations(
                if (it[0].value == "brick") {
                    null
                } else {
                    Registry.ITEM.get(it[0].bukkit())
                },
                if (it[1].value == "brick") {
                    null
                } else {
                    Registry.ITEM.get(it[1].bukkit())
                },
                if (it[2].value == "brick") {
                    null
                } else {
                    Registry.ITEM.get(it[2].bukkit())
                },
                if (it[3].value == "brick") {
                    null
                } else {
                    Registry.ITEM.get(it[3].bukkit())
                }
            )
        )
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.POT_DECORATIONS)
        Result.success(this to true)
    }
)

internal val gliderConverter = PaperDataAPIConverter<Glider>(
    {
        if (unwrap().hasData(DataComponentTypes.GLIDER)) {
            Result.success(Glider())
        }
        Result.success(null)
    }, {
        unwrap().setData(DataComponentTypes.GLIDER)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.GLIDER)
        Result.success(this to true)
    }
)

internal val rarityConverter = PaperDataAPIConverter(
    {
        val rarity = unwrap().getData(DataComponentTypes.RARITY)
        if (rarity == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        Result.success(Rarity.valueOf(rarity.toString()))
    }, {
        unwrap().setData(DataComponentTypes.RARITY, ItemRarity.valueOf(it.toString()))
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.RARITY)
        Result.success(this to true)
    }
)

internal val hideTooltipConverter = PaperDataAPIConverter(
    {
        Result.success(
            if (unwrap().hasData(DataComponentTypes.HIDE_TOOLTIP)) {
                HideTooltip()
            } else {
                null
            }
        )
    }, {
        unwrap().setData(DataComponentTypes.HIDE_TOOLTIP)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.HIDE_TOOLTIP)
        Result.success(this to true)
    }
)

internal val hideAdditionalTooltipConverter = PaperDataAPIConverter(
    {
        Result.success(
            if (unwrap().hasData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)) {
                HideAdditionalTooltip()
            } else {
                null
            }
        )
    }, {
        unwrap().setData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)
        Result.success(this to true)
    }
)

internal val trimConverter = PaperDataAPIConverter<Trim>(
    {
        val trim = unwrap().getData(DataComponentTypes.TRIM)
        if (trim == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        Result.success(
            TrimImpl(
                trim.armorTrim().pattern.key.toAPI(),
                trim.armorTrim().material.key.api()
            )
        )
    }, {

        val pattern = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).get(it.pattern.into());
        val material = RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(it.material.into())
        if (pattern == null || material == null) {
            return@PaperDataAPIConverter Result.failure(IllegalArgumentException("Cannot apply trim: Invalid pattern or material!"))
        }
        unwrap().setData(
            DataComponentTypes.TRIM, ItemArmorTrim.itemArmorTrim(
                ArmorTrim(material, pattern)
            )
        )
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.TRIM)
        Result.success(this to true)
    }
)