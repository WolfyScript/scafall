package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.common.api.wrappers.world.items.data.ToolImpl
import com.wolfyscript.scafall.common.api.wrappers.world.items.data.ToolRuleImpl
import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.toAPI
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
                        it.blocks().map { it.key().key().toAPI() },
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