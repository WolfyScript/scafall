package com.wolfyscript.scafall.common.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.data.Tool
import com.wolfyscript.scafall.wrappers.world.items.data.UseCooldown
import com.wolfyscript.scafall.wrappers.world.items.data.UseRemainder

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

data class UseCooldownCommon(
    override var seconds: Float,
    override var cooldownGroup: Key?
) : UseCooldown

data class UseRemainderCommon(override var stack: ItemStack) : UseRemainder