package com.wolfyscript.scafall.spigot.platform.persistent.events

import com.google.common.collect.ImmutableList
import com.wolfyscript.scafall.spigot.platform.persistent.world.BlockStorage
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

class BlockStorageMultiPlaceEvent(
    states: List<BlockState>,
    val blockStorages: List<BlockStorage>,
    clicked: Block,
    itemInHand: ItemStack,
    thePlayer: Player,
    canBuild: Boolean,
    hand: EquipmentSlot
) :
    BlockStoragePlaceEvent(
        states[0].block,
        blockStorages[0], states[0], clicked, itemInHand, thePlayer, canBuild, hand
    ),
    BlockStorageEvent {
    /**
     * Gets a list of blockstates for all blocks which were replaced by the
     * placement of the new blocks. Most of these blocks will just have a
     * Material type of AIR.
     *
     * @return immutable list of replaced BlockStates
     */
    val replacedBlockStates: List<BlockState> =
        ImmutableList.copyOf(states)
    override val storage: BlockStorage = blockStorages[0]
}
