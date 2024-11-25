package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.common.api.wrappers.world.items.data.ToolImpl
import com.wolfyscript.scafall.common.api.wrappers.world.items.data.ToolRuleImpl
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.Repairable
import com.wolfyscript.scafall.wrappers.world.items.data.Tool
import org.bukkit.Material

internal val toolItemMetaConverter = ItemMetaDataKeyConverter<Tool>(
    {
        if (hasTool()) {
            val tool = tool
            return@ItemMetaDataKeyConverter ToolImpl(
                tool.defaultMiningSpeed,
                tool.damagePerBlock,
                rules = tool.rules.map {
                    ToolRuleImpl(it.blocks.map { material -> material.key.toAPI() }, it.speed, it.isCorrectForDrops)
                }
            )
        }
        null
    },
    {
        val newTool = this.tool

        newTool.defaultMiningSpeed = it.defaultMiningSpeed
        newTool.damagePerBlock = it.damagePerBlock
        for (rule in it.rules) {
            newTool.addRule(rule.blocks.map { key -> Material.matchMaterial(key.toString()) }, rule.speed, rule.correctForDrops)
        }

        setTool(newTool)
    }
)

internal val repairableItemMetaConverter = ItemMetaDataKeyConverter<Repairable>(
    {
        null
    },
    {

    }
)

internal val tooltipStyleItemMetaConverter = ItemMetaDataKeyConverter<Key>(
    {
        if (hasTooltipStyle()) {
            return@ItemMetaDataKeyConverter tooltipStyle?.toAPI()
        }
        null
    },
    {
        tooltipStyle = it.bukkit()
    }
)