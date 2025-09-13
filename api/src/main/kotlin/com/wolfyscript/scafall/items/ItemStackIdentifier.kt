package com.wolfyscript.scafall.items

import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.wolfyscript.scafall.config.jackson.RegistryKeyTypeIdResolver
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike

/**
 * Identifies an item stack from an external source.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonTypeIdResolver(RegistryKeyTypeIdResolver::class)
@JsonPropertyOrder("type")
interface ItemStackIdentifier {

    /**
     * Checks if the given stack matches the stack represented by this identifier.
     */
    fun matches(stack: ItemStackLike, matchTags: Boolean): Boolean

    /**
     * Creates the item stack that this identifier represents by looking it up at the source.
     */
    fun create(): ItemStack

    /**
     * A parser that parses the identifier (and therefore the original source) of an existing item stack in the game.
     */
    interface Parser<T : ItemStackIdentifier> {

        /**
         * The priority of the parser. The result of the parser with the highest priority will be used.
         */
        val priority: Int

        /**
         * Tries to parse the original source of the given stack.
         *
         * @return The identifier of the stack or null if the stack is not recognized.
         */
        fun from(stack: ItemStackLike): T?

    }

}