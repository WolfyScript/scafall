package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.common.api.wrappers.world.items.data.ToolImpl
import com.wolfyscript.scafall.common.api.wrappers.world.items.data.ToolRuleImpl
import com.wolfyscript.scafall.common.api.wrappers.world.items.data.UseCooldownCommon
import com.wolfyscript.scafall.common.api.wrappers.world.items.data.UseRemainderCommon
import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.wrap
import com.wolfyscript.scafall.spigot.platform.world.items.actions.Data
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.UseCooldown
import com.wolfyscript.scafall.wrappers.world.items.data.UseRemainder
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Tool
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.set.RegistrySet
import net.kyori.adventure.util.TriState

internal val toolConverter = PaperDataAPIConverter<com.wolfyscript.scafall.wrappers.world.items.data.Tool>(
    {
        val tool = unwrap().getData(DataComponentTypes.TOOL)
        if (tool == null) {
            return@PaperDataAPIConverter Result.success(null)
        }

        Result.success(
            ToolImpl(
                tool.defaultMiningSpeed(),
                tool.damagePerBlock(),
                tool.rules().map {
                    ToolRuleImpl(
                        it.blocks().map { it.key().toAPI() },
                        it.speed(),
                        it.correctForDrops().toBoolean()
                    )
                })
        )
    }, {
        unwrap().setData(
            DataComponentTypes.TOOL, Tool.tool()
                .defaultMiningSpeed(it.defaultMiningSpeed)
                .damagePerBlock(it.damagePerBlock)
                .addRules(it.rules.map {
                    Tool.rule(RegistrySet.keySet(RegistryKey.BLOCK, it.blocks.map {
                        TypedKey.create(
                            RegistryKey.BLOCK, it.into()
                        )
                    }), it.speed, TriState.byBoolean(it.correctForDrops))
                })
        )

        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.TOOL)
        Result.success(this to true)
    }
)

internal val breakSoundConverter = PaperDataAPIConverter<SoundEvent>(
    {
        val soundKey = unwrap().getData(DataComponentTypes.BREAK_SOUND)
        if (soundKey == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        return@PaperDataAPIConverter Result.success(SoundEvent(soundKey.toAPI()))
    }, {
        unwrap().setData(DataComponentTypes.BREAK_SOUND, it.sound.into())
        return@PaperDataAPIConverter Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.BREAK_SOUND)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)

internal val useCooldownConverter = PaperDataAPIConverter<UseCooldown>(
    {
        val paperCooldown =
            unwrap().getData(DataComponentTypes.USE_COOLDOWN) ?: return@PaperDataAPIConverter Result.success(null)

        return@PaperDataAPIConverter Result.success(UseCooldownCommon(paperCooldown.seconds(), paperCooldown.cooldownGroup().toAPI()))
    }, {
        unwrap().setData(DataComponentTypes.USE_COOLDOWN, io.papermc.paper.datacomponent.item.UseCooldown.useCooldown(it.seconds).cooldownGroup(it.cooldownGroup?.into()))
        return@PaperDataAPIConverter Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.USE_COOLDOWN)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)

internal val useRemainderConverter = PaperDataAPIConverter<UseRemainder>(
    {
        val paperRemainder = unwrap().getData(DataComponentTypes.USE_REMAINDER) ?: return@PaperDataAPIConverter Result.success(null)
        return@PaperDataAPIConverter Result.success(UseRemainderCommon(paperRemainder.transformInto().wrap()))
    }, {
        unwrap().setData(DataComponentTypes.USE_REMAINDER, io.papermc.paper.datacomponent.item.UseRemainder.useRemainder(it.stack.unwrap()))
        return@PaperDataAPIConverter Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.USE_REMAINDER)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)


