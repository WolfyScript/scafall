package com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.itemsadder

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.inventory.ItemStack
import java.util.*

interface CustomBlock {
    fun place(location: Location?): Optional<CustomBlock>

    fun remove(): Boolean

    val baseBlockData: BlockData?

    val block: Block?

    val isPlaced: Boolean

    fun getLoot(includeSelfBlock: Boolean): List<ItemStack>

    val loot: List<ItemStack?>?

    fun getLoot(tool: ItemStack?, includeSelfBlock: Boolean): List<ItemStack>

    val originalLightLevel: Int

    fun setCurrentLightLevel(level: Int)

    fun playBreakEffect(): Boolean

    fun playBreakParticles(): Boolean

    fun playBreakSound(): Boolean

    fun playPlaceSound(): Boolean
}
