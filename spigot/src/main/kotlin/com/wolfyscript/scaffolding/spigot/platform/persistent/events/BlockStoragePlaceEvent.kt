package com.wolfyscript.scaffolding.spigot.platform.persistent.events

import com.wolfyscript.scaffolding.spigot.platform.persistent.world.BlockStorage
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

open class BlockStoragePlaceEvent(
    var block: Block, override val storage: BlockStorage,
    /**
     * Gets the BlockState for the block which was replaced. Material type air
     * mostly.
     *
     * @return The BlockState for the block which was replaced.
     */
    var blockReplacedState: BlockState,
    /**
     * Gets the block that this block was placed against
     *
     * @return Block the block that the new block was placed against
     */
    var blockAgainst: Block,
    /**
     * Gets the item in the player's hand when they placed the block.
     *
     * @return The ItemStack for the item in the player's hand when they
     * placed the block
     */
    var itemInHand: ItemStack,
    /**
     * Gets the player who placed the block involved in this event.
     *
     * @return The Player who placed the block involved in this event
     */
    var player: Player, protected var canBuild: Boolean,
    /**
     * Gets the hand which placed the block
     * @return Main or off-hand, depending on which hand was used to place the block
     */
    var hand: EquipmentSlot
) :
    Event(), BlockStorageEvent, Cancellable {
    protected var cancel: Boolean = false

    override fun isCancelled(): Boolean {
        return cancel
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }

    val blockPlaced: Block
        /**
         * Clarity method for getting the placed block. Not really needed except
         * for reasons of clarity.
         *
         * @return The Block that was placed
         */
        get() = block

    /**
     * Gets the value whether the player would be allowed to build here.
     * Defaults to spawn if the server was going to stop them (such as, the
     * player is in Spawn). Note that this is an entirely different check
     * than BLOCK_CANBUILD, as this refers to a player, not universe-physics
     * rule like cactus on dirt.
     *
     * @return boolean whether the server would allow a player to build here
     */
    fun canBuild(): Boolean {
        return this.canBuild
    }

    /**
     * Sets the canBuild state of this event. Set to true if you want the
     * player to be able to build.
     *
     * @param canBuild true if you want the player to be able to build
     */
    fun setBuild(canBuild: Boolean) {
        this.canBuild = canBuild
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        val handlerList: HandlerList = HandlerList()
    }
}
