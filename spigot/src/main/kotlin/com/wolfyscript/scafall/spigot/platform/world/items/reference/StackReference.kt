package com.wolfyscript.scafall.spigot.platform.world.items.reference

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdNodeBasedDeserializer
import com.wolfyscript.scafall.Copyable
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.ScafallProvider.Companion.get
import com.wolfyscript.scafall.dependency.DependencySource
import com.wolfyscript.scafall.dependency.MissingImplementationException
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.spigot.platform.stackIdentifierParsers
import com.wolfyscript.scafall.spigot.platform.world.items.ItemUtils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.io.IOException
import java.util.*
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Function

/**
 * Acts as a wrapper for [StackIdentifier], that links to an external ItemStack (like other Plugins).
 * This keeps track of the original ItemStack, as a fallback, and the parser used to get the wrapped [StackIdentifier].
 * Additionally, it stores the amount, and other extra settings.
 */
@JsonDeserialize(using = StackReference.Deserializer::class)
open class StackReference : Copyable<StackReference> {
    private val core: Scafall
    private var amount: Int
    private val weight: Double

    /**
     * Used to store the original stack
     */
    @JvmField
    @JsonIgnore
    protected var stack: ItemStack?

    /**
     * Used to store the previous parser result
     */
    @JvmField
    @DependencySource
    protected var identifier: StackIdentifier?

    private var parserKey: Key
    private var parser: StackIdentifierParser<*>? = null

    constructor(core: Scafall, amount: Int, weight: Double, identifier: StackIdentifier, itemStack: ItemStack?) {
        this.core = core
        this.amount = amount
        this.weight = weight
        this.identifier = identifier
        this.parser = identifier.parser()
        this.parserKey = parser!!.key()
        this.stack = itemStack
    }

    constructor(core: Scafall, amount: Int, weight: Double, parserKey: Key, item: ItemStack?) {
        this.amount = amount
        this.weight = weight
        this.core = core
        this.parserKey = parserKey
        this.stack = item // Set item before parsing it
        this.identifier = this.orParseIdentifier
    }

    private constructor(stackReference: StackReference) {
        this.weight = stackReference.weight
        this.amount = stackReference.amount
        this.core = stackReference.core
        this.parserKey = stackReference.parserKey
        this.stack = stackReference.stack
        this.identifier = this.orParseIdentifier
    }

    /**
     * Swaps the current parser with the specified parser and parses the original stack to get the new StackIdentifier.
     *
     * @param parser The new parser to use to get the StackIdentifier
     */
    fun swapParser(parser: StackIdentifierParser<*>) {
        this.parserKey = parser.key()
        this.identifier = null // Reset cached identifier, so it gets parsed again
        this.identifier = this.orParseIdentifier
    }

    @get:JsonGetter("identifier")
    protected open val orParseIdentifier: StackIdentifier?
        /**
         * Parses the identifier when it is not available, or returns the current identifier.
         *
         * @return The parsed Identifier, or null if not available.
         */
        get() {
            if (identifier == null) {
                identifier = parser().from(stack)
            }
            return identifier
        }

    /**
     * Gets the currently used [StackIdentifierParser]
     *
     * @return The current [StackIdentifierParser]
     */
    fun parser(): StackIdentifierParser<*> {
        if (parser == null || parser!!.key() != parserKey) {
            parser = core.registries.stackIdentifierParsers[parserKey]
        }
        if (parser == null) {
            throw MissingImplementationException("Could not find stack identifier parser $parserKey")
        }
        return parser!! // Parser is still cached and wasn't changed
    }

    /**
     * Gets the currently wrapped StackIdentifier, parsed by the current [StackIdentifierParser]
     *
     * @return The currently wrapped StackIdentifier
     */
    fun identifier(): Optional<StackIdentifier> {
        return Optional.ofNullable(orParseIdentifier)
    }

    @JvmOverloads
    fun matches(other: ItemStack, exact: Boolean = true, ignoreAmount: Boolean = false): Boolean {
        if (ItemUtils.isAirOrNull(other)) return false
        if (!ignoreAmount && other.amount < amount) return false
        return identifier().map { identifier: StackIdentifier ->
            identifier.matchesIgnoreCount(
                other,
                exact
            )
        }.orElse(false)
    }

    /**
     * Convenience method to get the stack the identifier points to.<br></br>
     * This is the same as `[identifier()][.identifier].[stack][StackIdentifier.stack]([ItemCreateContext.of]([this][StackReference]).[build()][ItemCreateContext.Builder.build])`
     *
     * @return The stack the [.identifier] points to
     */
    fun referencedStack(): ItemStack {
        return identifier().map { stackIdentifier: StackIdentifier ->
            stackIdentifier.stack(ItemCreateContext.of(this).build())
        }.orElse(ItemStack(Material.AIR))!!
    }

    /**
     * Convenience method to get the stack the identifier points to.<br></br>
     * This is the same as `[identifier()][.identifier].[stack][StackIdentifier.stack]([ItemCreateContext.of]([this][StackReference]).[build()][ItemCreateContext.Builder.build])`
     *
     * @param contextBuild provides a [ItemCreateContext.Builder] with this reference already applied
     * @return The stack the [.identifier] points to
     */
    fun referencedStack(contextBuild: Consumer<ItemCreateContext.Builder?>): ItemStack {
        return identifier().map { stackIdentifier: StackIdentifier ->
            val builder = ItemCreateContext.of(this)
            contextBuild.accept(builder)
            stackIdentifier.stack(builder.build())
        }.orElse(ItemStack(Material.AIR))
    }

    /**
     * Gets the **ORIGINAL** stack, from which this reference was created from!<br></br>
     * For the linked stack from for example an external plugin use [.identifier]!
     *
     * @return The **ORIGINAL** stack this reference was created from
     * @see .identifier
     * @see .referencedStack
     */
    open fun originalStack(): ItemStack? {
        return stack
    }

    /**
     * Gets the weight associated with this reference inside a collection.<br></br>
     * For example inside of a [<]
     *
     * @return The weight of this reference
     */
    @JsonGetter("weight")
    fun weight(): Double {
        return weight
    }

    /**
     * Gets the stack amount for the referenced ItemStack
     *
     * @return The stack amount of the referenced ItemStack
     */
    @JsonGetter("amount")
    fun amount(): Int {
        return amount
    }

    fun setAmount(amount: Int) {
        this.amount = amount
    }

    override fun copy(): StackReference {
        return StackReference(this)
    }

    /**
     * Shrinks the specified stack by the given amount and returns the manipulated or replaced item!
     * <br></br><br></br>
     * <h3>Stackable  ([.originalStack].[getMaxStackSize()][ItemStack.getMaxStackSize] > 1 or stack count > 1):</h3>
     *
     *
     * The stack is shrunk by the specified amount (**`[.amount] * totalAmount`**).<br></br>
     * For applying stackable replacements it calls the stackReplacement function with the already shrunken stack and this reference.<br></br>
     * Default behaviour can be found here:
     *
     *  * [.shrink]
     *  * [.shrinkUnstackableItem]
     *
     *
     * <h3>Un-stackable  ([.originalStack].[getMaxStackSize()][ItemStack.getMaxStackSize] == 1 and stack count == 1):</h3>
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
    ): ItemStack {
        return identifier().map {
            it.shrink(stack, count * amount(), useRemains, stackReplacement)
        }.orElse(stack)!!
    }

    /**
     * Shrinks the specified stack by the given amount and returns the manipulated or replaced item!
     *
     *
     * <h3>Stackable  ([.originalStack].[getMaxStackSize()][ItemStack.getMaxStackSize] > 1 or stack count > 1):</h3>
     * The stack is shrunk by the specified amount (**`[.amount] * count`**)
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
     * <h3>Un-stackable  ([.originalStack].[getMaxStackSize()][ItemStack.getMaxStackSize] == 1 and stack count == 1):</h3>
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
    ): ItemStack {
        return identifier().map {
            it.shrink(stack, count * amount(), useRemains, inventory, player, location)
        }.orElse(stack)!!
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
    fun shrinkUnstackableItem(stack: ItemStack, useRemains: Boolean): ItemStack {
        return identifier().map { it.shrinkUnstackableItem(stack, useRemains) }.orElse(stack)!!
    }


    fun shrinkUnstackableItem(
        stack: ItemStack,
        useRemains: Boolean,
        remainsFunction: BiFunction<StackIdentifier?, ItemStack?, ItemStack?>,
        manipulator: Function<ItemStack?, ItemStack?>
    ): ItemStack {
        return identifier().map { stackIdentifier -> stackIdentifier.shrinkUnstackableItem(stack, useRemains, remainsFunction, manipulator) }.orElse(stack)!!
    }

    class Deserializer private constructor() :
        StdNodeBasedDeserializer<StackReference>(StackReference::class.java) {
        private val core =
            get()

        @Throws(IOException::class)
        override fun convert(root: JsonNode, ctxt: DeserializationContext): StackReference {
            val hasIdentifier = root.has("identifier")
            if (hasIdentifier || root.has("parser")) {
                var weight = root["weight"].asDouble(1.0)
                weight =
                    if (weight <= 0) 1.0 else weight // make sure weight is greater than 0, so it never disappears unintentionally (e.g. in Recipe results)!
                val amount = root["amount"].asInt(1)

                // New stack references format saves the identifier directly
                // But can still optionally contain the original stack
                var originalStack: ItemStack? = null
                if (root.has("stack")) {
                    originalStack = ctxt.readTreeAsValue(root["stack"], ItemStack::class.java)
                }

                if (hasIdentifier) {
                    // Identifier is defined, use defined identifier
                    val identifier = ctxt.readTreeAsValue(
                        root["identifier"],
                        StackIdentifier::class.java
                    )
                    if (originalStack == null) {
                        originalStack = identifier.stack(ItemCreateContext.empty(1))
                    }
                    return StackReference(core, amount, weight, identifier, originalStack)
                }

                // Parser is defined, parse form original stack
                val parserKey = ctxt.readTreeAsValue(
                    root["parser"],
                    Key::class.java
                )
                return StackReference(core, amount, weight, parserKey, originalStack)
            }

            // Unknown type
            return of(ItemUtils.AIR)
        }
    }

    companion object {
        fun of(itemStack: ItemStack): StackReference {
            return StackReference(get(), itemStack.amount, 1.0, BukkitStackIdentifier(itemStack), itemStack)
        }
    }
}
