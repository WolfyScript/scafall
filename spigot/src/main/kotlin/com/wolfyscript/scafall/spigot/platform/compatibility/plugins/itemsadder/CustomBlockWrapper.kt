package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.itemsadder

import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.inventory.ItemStack
import java.util.*

class CustomBlockWrapper private constructor(private val iaBlock: dev.lone.itemsadder.api.CustomBlock) : CustomBlock {
    override fun place(location: Location?): Optional<CustomBlock> {
        return Optional.ofNullable(
            wrapNullableBlock(
                iaBlock.place(location)
            )
        )
    }

    override fun remove(): Boolean {
        return iaBlock.remove()
    }

    override val baseBlockData: BlockData?
        get() = iaBlock.baseBlockData

    override val block: Block
        get() = iaBlock.block

    override val isPlaced: Boolean
        get() = iaBlock.isPlaced

    override fun getLoot(includeSelfBlock: Boolean): List<ItemStack> {
        return iaBlock.getLoot(includeSelfBlock)
    }

    override val loot: List<ItemStack>
        get() = iaBlock.loot

    override fun getLoot(tool: ItemStack?, includeSelfBlock: Boolean): List<ItemStack> {
        return iaBlock.getLoot(tool, includeSelfBlock)
    }

    override val originalLightLevel: Int
        get() = iaBlock.originalLightLevel

    override fun setCurrentLightLevel(level: Int) {
        iaBlock.setCurrentLightLevel(level)
    }

    override fun playBreakEffect(): Boolean {
        return iaBlock.playBreakEffect()
    }

    override fun playBreakParticles(): Boolean {
        return iaBlock.playBreakParticles()
    }

    override fun playBreakSound(): Boolean {
        return iaBlock.playBreakSound()
    }

    override fun playPlaceSound(): Boolean {
        return iaBlock.playPlaceSound()
    }

    companion object {
        fun wrapBlock(iaBlock: dev.lone.itemsadder.api.CustomBlock?): Optional<CustomBlock> {
            return Optional.ofNullable(wrapNullableBlock(iaBlock))
        }

        private fun wrapNullableBlock(iaBlock: dev.lone.itemsadder.api.CustomBlock?): CustomBlockWrapper? {
            return if (iaBlock != null) CustomBlockWrapper(iaBlock) else null
        }
    }
}
