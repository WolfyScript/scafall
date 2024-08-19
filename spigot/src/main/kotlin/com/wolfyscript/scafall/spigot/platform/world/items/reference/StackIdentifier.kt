package com.wolfyscript.scafall.spigot.platform.world.items.reference

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver
import com.wolfyscript.scafall.ScafallProvider.Companion.get
import com.wolfyscript.scafall.config.jackson.KeyedTypeIdResolver
import com.wolfyscript.scafall.config.jackson.KeyedTypeResolver
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Keyed
import com.wolfyscript.scafall.spigot.platform.stackIdentifierParsers
import com.wolfyscript.scafall.spigot.platform.world.items.CustomItem.Companion.changeDurability
import com.wolfyscript.scafall.spigot.platform.world.items.CustomItem.Companion.craftRemain
import com.wolfyscript.scafall.spigot.platform.world.items.CustomItem.Companion.getByItemStack
import com.wolfyscript.scafall.spigot.platform.world.items.ItemUtils
import com.wolfyscript.scafall.spigot.platform.world.items.reference.ItemCreateContext.Companion.empty
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.function.BiFunction
import java.util.function.Function
import kotlin.math.min

@JsonTypeResolver(KeyedTypeResolver::class)
@JsonTypeIdResolver(
    KeyedTypeIdResolver::class
)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "key")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder(value = ["key"])
interface StackIdentifier : Keyed {
    /**
     * Creates an item stack, this identifier points to, using the provided [ItemCreateContext].<br></br>
     * The context can be used to create player specific stacks, specify the stack amount, and more.
     *
     * @param context The context for the item stack creation
     * @return The item stack, that this identifier points to, created using the provided context
     */
    fun stack(context: ItemCreateContext): ItemStack

    /**
     * Checks if the specified item stack matches the reference stack from this identifier.
     *
     * @param other The other stack to check for a match
     * @return true if the other stack matches this identifier
     */
    fun matches(other: ItemStack, exact: Boolean = true): Boolean {
        return matches(other, 1, exact)
    }

    fun matches(other: ItemStack, count: Int, exact: Boolean): Boolean {
        if (ItemUtils.isAirOrNull(other)) return false
        if (other.amount < count) return false
        return matchesIgnoreCount(other, exact)
    }

    fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean

    /**
     * Gets the optional permission string this identifier requires
     *
     * @return Optional containing the permission string, or empty if not available
     */
    fun permission(): Optional<String> {
        return Optional.empty()
    }

    /**
     * Shrinks the specified stack by the given amount and returns the manipulated or replaced item!
     * <br></br><br></br>
     * <h3>Stackable  ([.stack].[getMaxStackSize()][ItemStack.getMaxStackSize] > 1 or stack count > 1):</h3>
     *
     *
     * The stack is shrunk by the specified amount (**`[.stack].[getAmount()][ItemStack.getAmount] * count`**).<br></br>
     * For applying stackable replacements it calls the stackReplacement function with the already shrunken stack and this reference.<br></br>
     * Default behaviour can be found here:
     *
     *  * [.shrink]
     *  * [.shrinkUnstackableItem]
     *
     *
     * <h3>Un-stackable  ([.stack].[getMaxStackSize()][ItemStack.getMaxStackSize] == 1 and stack count == 1):</h3>
     *
     *
     * Redirects to [.shrinkUnstackableItem]<br></br>
     *
     *
     * @param stack            The input ItemStack, that is also going to be edited.
     * @param count            The amount of this custom item that should be removed from the input.
     * @param useRemains       If the Item should be replaced by the default craft remains.
     * @param stackReplacement Behaviour of how to apply the replacements of stackable items.
     * @return The manipulated stack, default remain, or custom remains.
     */
    fun shrink(
        stack: ItemStack,
        count: Int,
        useRemains: Boolean,
        stackReplacement: BiFunction<StackIdentifier?, ItemStack, ItemStack?>
    ): ItemStack? {
        var stack = stack
        if (stack.maxStackSize == 1 && stack.amount == 1) {
            return shrinkUnstackableItem(stack, useRemains)
        }
        val amount = stack.amount - count
        if (amount <= 0) {
            stack = ItemStack(Material.AIR)
        } else {
            stack.amount = amount
        }
        return stackReplacement.apply(this, stack)
    }

    /**
     * Shrinks the specified stack by the given amount and returns the manipulated or replaced item!
     *
     *
     * <h3>Stackable  ([.stack].[getMaxStackSize()][ItemStack.getMaxStackSize] > 1 or stack count > 1):</h3>
     * The stack is shrunk by the specified amount (**`[.stack].[getAmount()][ItemStack.getAmount] * count`**)
     *
     *
     * If this stack has craft remains:<br></br>
     *
     *  * **Location: **Used as the drop location for remaining items. <br></br>May be overridden by options below.
     *  *
     * **Player: **Adds items to the players inventory.
     * <br></br>Remaining items are still in the pool for the next options below.
     * <br></br>Player location is used as the drop location for remaining items.
     *  *
     * **Inventory:** Adds items to the inventory.
     * <br></br>Remaining items are still in the pool for the next options below.
     * <br></br>If location not available yet: uses inventory location as drop location for remaining items.
     *
     *
     * All remaining items that cannot be added to player or the other inventory are dropped at the specified location.<br></br>
     * **Warning! If you do not provide a location via `player`, `inventory`, or `location`, then the remaining items are discarded!**<br></br>
     * For custom behaviour see [.shrink].
     *
     *
     *
     *
     * <h3>Un-stackable  ([.stack].[getMaxStackSize()][ItemStack.getMaxStackSize] == 1 and stack count == 1):</h3>
     * Redirects to [.shrinkUnstackableItem]<br></br>
     *
     *
     * <br></br>
     *
     * @param stack      The input ItemStack, that is also going to be edited.
     * @param count      The amount of this custom item that should be removed from the input.
     * @param useRemains If the Item should be replaced by the default craft remains.
     * @param inventory  The optional inventory to add the replacements to. (Only for stackable items)
     * @param player     The player to give the items to. If the players' inventory has space the craft remains are added. (Only for stackable items)
     * @param location   The location where the replacements should be dropped. (Only for stackable items)
     * @return The manipulated stack, default remain, or custom remains.
     */
    fun shrink(
        stack: ItemStack,
        count: Int,
        useRemains: Boolean,
        inventory: Inventory?,
        player: Player?,
        location: Location?
    ): ItemStack? {
        return shrink(stack, count, useRemains) { _, resultStack ->
            var remains: ItemStack? = null
            // Use custom remains options if it is a custom item
            val customItem = getByItemStack(stack)
            if (customItem != null) {
                remains = if (!customItem.isConsumed) stack else customItem.replacement()?.referencedStack()
            }
            if (remains == null) {
                remains = if (useRemains) craftRemain(stack)?.let { ItemStack(it) } else null
            }
            remains?.let { replacement ->
                var replacement = replacement
                var originalStack = resultStack
                var replacementAmount = count
                if (ItemUtils.isAirOrNull(originalStack)) {
                    val returnableAmount = min(replacement.maxStackSize.toDouble(), replacementAmount.toDouble()).toInt()
                    replacementAmount -= returnableAmount
                    originalStack = replacement.clone()
                    originalStack.amount = replacementAmount
                }
                if (replacementAmount > 0) {
                    replacement.amount = replacementAmount
                    var loc = location
                    if (player != null) {
                        replacement = player.inventory.addItem(replacement)[0]!!
                        loc = player.location
                    }
                    if (inventory != null) {
                        replacement = inventory.addItem(replacement)[0]!!
                        if (loc == null) loc = inventory.location
                    }
                    if (loc != null && loc.world != null) {
                        loc.world.dropItemNaturally(loc.add(0.5, 1.0, 0.5), replacement)
                    }
                }
                originalStack
            } ?: resultStack
        }
    }

    /**
     * Shrinks the specified stack and returns the manipulated or replaced item!
     *
     *
     * This firstly checks for custom replacements (remains) and sets it as the result.<br></br>
     * Then handles damaging of the stack, if there is a specified durability cost.<br></br>
     * In case the stack breaks due damage it is replaced by the result, specified earlier.
     *
     *
     * @param stack      The stack to shrink
     * @param useRemains If the Item should be replaced by the default craft remains.
     * @return The manipulated (damaged) stack, default remain, or custom remains.
     */
    fun shrinkUnstackableItem(
        stack: ItemStack,
        useRemains: Boolean,
        remainsFunction: BiFunction<StackIdentifier?, ItemStack?, ItemStack?> = BiFunction { _, itemStack ->
            val customItem = getByItemStack(itemStack)
            if (customItem != null) {
                if (!customItem.isConsumed) return@BiFunction stack
                return@BiFunction customItem.replacement()?.referencedStack()
            }
            null
        },
        manipulator: Function<ItemStack?, ItemStack?> = Function { result ->
            val customItem = getByItemStack(result)
            if (customItem != null) {
                return@Function changeDurability(customItem, stack, result)
            }
            result
        }
    ): ItemStack? {
        return manipulator.apply(
            remainsFunction.apply(this, stack)?.let { stack(empty(1)) }
                ?: run {
                    if (useRemains) return@run craftRemain(stack)?.let { ItemStack(it) }
                    null
                }
        ) ?: ItemStack(Material.AIR)
    }

    fun parser(): StackIdentifierParser<*> {
        return get().registries.stackIdentifierParsers[key()]!!
    }

    @JsonProperty("key")
    @JsonIgnore
    override fun key(): Key
}
