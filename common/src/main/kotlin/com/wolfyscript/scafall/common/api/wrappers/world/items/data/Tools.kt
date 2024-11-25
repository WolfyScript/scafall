package com.wolfyscript.scafall.common.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.data.Tool

class ToolImpl(
    override val defaultMiningSpeed: Float,
    override val damagePerBlock: Int,
    override val rules: List<Tool.Rule>
) : Tool

class ToolRuleImpl(
    override val blocks: List<Key>,
    override val speed: Float?,
    override val correctForDrops: Boolean?
) : Tool.Rule