/*
 *       WolfyUtilities, APIs and Utilities for Minecraft Spigot plugins
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.wolfyscript.scaffolding.spigot.platform.world.items

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.common.collect.Streams
import com.wolfyscript.scaffolding.ScaffoldingProvider
import com.wolfyscript.scaffolding.config.jackson.JacksonUtil.objectMapper
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.parse
import com.wolfyscript.scaffolding.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scaffolding.identifier.Keyed
import com.wolfyscript.scaffolding.spigot.api.compatibilityManager
import com.wolfyscript.scaffolding.spigot.api.identifiers.bukkit
import com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.itemsadder.ItemsAdderIntegration
import com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.itemsadder.ItemsAdderStackIdentifier
import com.wolfyscript.scaffolding.spigot.platform.customItemData
import com.wolfyscript.scaffolding.spigot.platform.customItems
import com.wolfyscript.scaffolding.spigot.platform.particleAnimations
import com.wolfyscript.scaffolding.spigot.platform.particles.ParticleLocation
import com.wolfyscript.scaffolding.spigot.platform.stackIdentifierParsers
import com.wolfyscript.scaffolding.spigot.platform.world.items.meta.CustomItemTagMeta
import com.wolfyscript.scaffolding.spigot.platform.world.items.meta.Meta
import com.wolfyscript.scaffolding.spigot.platform.world.items.meta.MetaSettings
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.util.*
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Function

/**
 *
 *
 * This Object acts as a wrapper for the [APIReference] with additional options that make it possible to manipulate the behaviour of the wrapped reference.
 * <br></br>
 * The [APIReference] can be any kind of reference, a simple [ItemStack] ([VanillaRef]) or an item from another API.
 *
 *
 *
 * For most additional features the CustomItem has to be registered into the [Registries.getCustomItems].
 * <br></br>
 * To make sure the CustomItem can be detected later on, it must be created via any of the [.create] methods.
 * <br></br>
 * These methods will include an extra [PersistentDataContainer] entry to identify the item later on!
 *
 */
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
class CustomItem : AbstractItemBuilder<CustomItem>, Keyed {
    @JsonIgnore
    private val type: Material

    /**
     * Other than [.create] it returns the real item and no copy!
     * Any changes made to this item may change the source Item!
     *
     * @return the linked item of the API reference
     */
    override val itemStack: ItemStack
        get() = reference.referencedStack()

    /**
     * This namespacedKey can either be null or non-null.
     *
     *
     * If it's non-null, the item is saved and the variables of this Object will be persistent
     * when converted to ItemStack via [.create].
     *
     *
     * If it is null, the item isn't saved and the variables of this Object will get lost when [.create] is called!
     */
    @JsonIgnore
    private val namespacedKey: Key

    @JsonIgnore
    private val craftRemain: Material?

    @JsonAlias("api_reference", "apiReference", "item")
    private val reference: StackReference

    @JsonIgnore
    private val indexedData: MutableMap<Key, CustomItemData> = HashMap()

    @JsonAlias("equipment_slots")
    private val equipmentSlots: MutableList<EquipmentSlot>

    /**
     * @return True if the item is removed by calling [.remove].
     */
    var isConsumed: Boolean

    /**
     * Returns if this item is blocked to be equipped.
     * If true the item cannot be equipped even if it is a chestplate or other equipment.
     *
     * @return true if the item is blocked from equipping, else false
     */
    var isBlockVanillaEquip: Boolean

    /**
     * BlockPlacement indicates if the item can be placed by a player or not.
     * If true the placement is blocked and the item cannot be placed.
     * If false the item can be placed.
     *
     * @return true if the placement is blocked, false otherwise
     */
    var isBlockPlacement: Boolean

    /**
     * Returns if this item is blocked in vanilla recipes.
     * This requires CustomCrafting to work.
     *
     * @return true if this item is blocked in vanilla recipes, else false
     */
    var isBlockVanillaRecipes: Boolean
    /**
     * Gets the permission string of this CustomItem.
     *
     * @return The permission string of this item
     */
    /**
     * Sets the permission String.
     *
     * @param permission The new permission string
     */
    var permission: String
    private var nbtChecks: MetaSettings? = null

    private var replacement: StackReference?

    @JsonAlias("fuel")
    var fuelSettings: FuelSettings

    /**
     * @return The durability that is removed from the item when removed from an inventory using [.remove]
     */
    @JsonAlias("durability_cost")
    var durabilityCost: Int

    @get:JsonGetter
    @JsonAlias("particles")
    var particleContent: ParticleContent
        private set

    @get:JsonGetter
    @set:JsonSetter
    var actionSettings: ActionSettings = ActionSettings()

    val blockSettings: CustomBlockSettings

    @JsonIgnore
    private var checkOldMetaSettings = true

    @JsonCreator
    constructor(
        key: Key,
        @JsonProperty("reference") reference: StackReference
    ) : super(
        CustomItem::class.java
    ) {
        this.namespacedKey = key
        this.reference = reference

        this.fuelSettings = FuelSettings()
        metaSettings = MetaSettings()
        this.permission = ""

        this.equipmentSlots = ArrayList()
        this.particleContent = ParticleContent()
        this.blockSettings = CustomBlockSettings()
        this.isBlockPlacement = false
        this.isBlockVanillaEquip = false
        this.isBlockVanillaRecipes = false

        this.isConsumed = true
        this.replacement = null
        this.durabilityCost = 0
        this.type = itemStack.type
        this.craftRemain = getCraftRemain()
    }

    @JsonAnySetter
    @Throws(JsonProcessingException::class)
    protected fun setOldProperties(fieldKey: String, value: JsonNode) {
        if (fieldKey == "advanced") {
            checkOldMetaSettings = value.asBoolean()
        } else if (fieldKey == "metaSettings" || fieldKey == "meta") {
            //Since the new system has its new field we need to update old appearances to the new system.
            val node = if (value.isTextual) objectMapper.readTree(value.asText()) else value
            val checks: MutableList<Meta>
            if (!node.has(MetaSettings.CHECKS_KEY)) {
                checks = ArrayList()
                if (checkOldMetaSettings) {
                    //Convert old meta to new format.
                    node.fields().forEachRemaining { entry: Map.Entry<String, JsonNode?> ->
                        val value = entry.value
                        if (value is ObjectNode) {
                            val key = entry.key.lowercase()
                            val namespacedKey =
                                if (key.contains(":")) parse(key) else scaffoldingKey(
                                    key
                                )
                            value.put("key", namespacedKey.toString())
                            val meta = objectMapper.convertValue(value, Meta::class.java)
                            if (meta != null && meta.option != MetaSettings.Option.IGNORE && meta.option != MetaSettings.Option.EXACT) {
                                checks.add(meta)
                            }
                        }
                    }
                }
            } else {
                checks =
                    objectMapper.convertValue(node[MetaSettings.CHECKS_KEY], object : TypeReference<List<Meta>>() {})
                        .toMutableList()
            }
            val nbtChecks = MetaSettings()
            checks.forEach(Consumer { meta: Meta? ->
                nbtChecks.addCheck(
                    meta!!
                )
            })
            metaSettings = nbtChecks
        }
    }

    /**
     * Creates a CustomItem with a Vanilla Reference to the itemstack
     *
     * @param itemStack the itemstack this CustomItem will be linked to
     */
    constructor(key: Key, itemStack: ItemStack) : this(
        key,
        StackReference(ScaffoldingProvider.get(), 1, 1.0, BukkitStackIdentifier.ID, itemStack)
    )

    /**
     * Creates a CustomItem with a Vanilla Reference to an itemstack of the material
     *
     * @param material the material of the itemstack this CustomItem will be linked to
     */
    constructor(key: Key, material: Material) : this(key, ItemStack(material))

    /**
     * @param customItem A new deep copy of the passed in CustomItem.
     */
    private constructor(customItem: CustomItem) : super(CustomItem::class.java) {
        this.reference = customItem.reference.copy()

        this.namespacedKey = customItem.key()
        this.fuelSettings = customItem.fuelSettings.copy()
        this.blockSettings = customItem.blockSettings.copy()
        this.nbtChecks = customItem.nbtChecks
        this.permission = customItem.permission
        indexedData.clear()
        for ((key, value) in customItem.indexedData) {
            indexedData[key] = value.copy()
        }
        this.equipmentSlots = ArrayList(customItem.equipmentSlots)
        this.particleContent = customItem.particleContent
        this.isBlockPlacement = customItem.isBlockPlacement
        this.isBlockVanillaEquip = customItem.isBlockVanillaEquip
        this.isBlockVanillaRecipes = customItem.isBlockVanillaRecipes

        this.isConsumed = customItem.isConsumed
        this.replacement = customItem.replacement
        this.durabilityCost = customItem.durabilityCost
        this.type = itemStack.type
        this.craftRemain = getCraftRemain()
    }

    /**
     * Clones the CustomItem and all the containing data.
     *
     * @return An exact deep copy of this CustomItem instance.
     */
    fun clone(): CustomItem {
        return CustomItem(this)
    }

    fun hasNamespacedKey(): Boolean {
        return namespacedKey != null
    }

    override fun key(): Key {
        return namespacedKey
    }

    var metaSettings: MetaSettings?
        get() = nbtChecks
        set(nbtChecks) {
            if (nbtChecks!!.isEmpty) {
                //Add the CustomItemTag check, so the item will be checked correctly, but only if the item hasn't got any other checks already.
                nbtChecks.addCheck(CustomItemTagMeta())
            }
            this.nbtChecks = nbtChecks
        }

    /**
     * @return The EquipmentSlots this item can be equipped to.
     */
    fun getEquipmentSlots(): List<EquipmentSlot> {
        return equipmentSlots
    }

    /**
     * @return True if the item has a custom [EquipmentSlot] it can be equipped to.
     */
    fun hasEquipmentSlot(): Boolean {
        return getEquipmentSlots().isNotEmpty()
    }

    fun hasEquipmentSlot(slot: EquipmentSlot): Boolean {
        return hasEquipmentSlot() && getEquipmentSlots().contains(slot)
    }

    fun addEquipmentSlots(vararg slots: EquipmentSlot) {
        for (slot in slots) {
            if (!equipmentSlots.contains(slot)) {
                equipmentSlots.add(slot)
            }
        }
    }

    fun removeEquipmentSlots(vararg slots: EquipmentSlot) {
        equipmentSlots.removeAll(listOf(*slots))
    }

    /**
     * Checks if the ItemStack is a similar to this CustomItem.
     * This method checks all the available ItemMeta on similarity and uses the meta options
     * when they are available.
     * Use [.isSimilar] to only check for Material and Amount!
     *
     * @param otherItem the ItemStack that should be checked
     * @return true if the ItemStack is exactly the same as this CustomItem's ItemStack
     */
    fun isSimilar(otherItem: ItemStack?): Boolean {
        return isSimilar(otherItem, true)
    }

    /**
     * Checks if the ItemStack is similar to this CustomItem.
     *
     * If exactMeta is false it only checks for Material and amount.
     *
     * If exactMeta is true it checks all the available ItemMeta and uses the meta options
     * when they are available.
     *
     * @param otherItem the ItemStack that should be checked
     * @param exactMeta if the ItemMeta should be checked. If false only checks Material and Amount!
     * @return true if the ItemStack is equal to this CustomItems ItemStack
     */
    fun isSimilar(otherItem: ItemStack?, exactMeta: Boolean): Boolean {
        return isSimilar(otherItem, exactMeta, false)
    }

    /**
     * Checks if the ItemStack is similar to this CustomItem.
     *
     *
     *
     * **Exact Meta:**
     *
     *  * false - Only checks for Material and amount (if ignoreAmount isn't enabled!).
     *  * true - Checks all the available ItemMeta and uses the meta options when they are available.
     *
     *
     *
     * <br></br>
     *
     * @param otherItem    the ItemStack that should be checked
     * @param exactMeta    if the ItemMeta should be checked. If false only checks Material and Amount!
     * @param ignoreAmount If true ignores the amount check.
     * @return true if the ItemStack is equal to this CustomItems ItemStack
     */
    fun isSimilar(otherItem: ItemStack?, exactMeta: Boolean, ignoreAmount: Boolean): Boolean {
        if (otherItem != null && otherItem.type == this.type && (ignoreAmount || otherItem.amount >= amount)) {
            if (hasNamespacedKey()) {
                return metaSettings!!.check(this, ItemBuilder(otherItem))
            } else if (reference.identifier().get() is BukkitStackIdentifier && (!hasItemMeta() && !exactMeta)) {
                return true
            }
            return reference.matches(otherItem)
        }
        return false
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is CustomItem) return false
        return durabilityCost == o.durabilityCost && isConsumed == o.isConsumed && isBlockPlacement == o.isBlockPlacement && isBlockVanillaEquip == o.isBlockVanillaEquip && isBlockVanillaRecipes == o.isBlockVanillaRecipes &&
                namespacedKey == o.namespacedKey &&
                replacement == o.replacement &&
                fuelSettings == o.fuelSettings &&
                permission == o.permission &&
                equipmentSlots == o.equipmentSlots &&
                reference == o.reference &&
                particleContent == o.particleContent &&
                nbtChecks == o.nbtChecks
    }

    override fun hashCode(): Int {
        return Objects.hash(
            key(), replacement,
            permission,
            fuelSettings,
            blockSettings,
            durabilityCost,
            isConsumed,
            isBlockPlacement,
            isBlockVanillaEquip,
            isBlockVanillaRecipes, getEquipmentSlots(), reference,
            particleContent,
            metaSettings
        )
    }

    /**
     * Gets a copy of the current version of the external linked item.<br></br>
     * The item can be linked to Vanilla, WolfyUtilities, ItemsAdder, Oraxen, MythicMobs or MMOItems
     *
     * @return the item from the external API that is linked to this object
     */
    override fun create(): ItemStack {
        return create(amount)
    }

    /**
     * Gets a copy of the current version of the external linked item.<br></br>
     * The item can be linked to Vanilla, WolfyUtilities, ItemsAdder, Oraxen, MythicMobs or MMOItems.
     *
     *
     * If this CustomItem has an NamespacedKey it will include it in the NBT of the returned item!
     *
     *
     * @param amount Modifies the amount of the returned ItemStack.
     * @return the item from the external API that is linked to this object
     */
    fun create(amount: Int): ItemStack {
        return reference.identifier().map { stackIdentifier: StackIdentifier ->
            val itemStack = stackIdentifier.stack(ItemCreateContext.empty(amount))?.clone() ?: return@map ItemStack(Material.AIR)
            if (this.hasNamespacedKey()) {
                val itemMeta = itemStack.itemMeta
                val container = itemMeta.persistentDataContainer
                synchronized(container.javaClass) { // The container has a thread-unsafe map usage, so we need to synchronise it
                    container.set(
                        scaffoldingKey("custom_item").bukkit(),
                        PersistentDataType.STRING,
                        namespacedKey.toString()
                    )
                }
                itemStack.setItemMeta(itemMeta)
            }
            if (amount > 0) {
                itemStack.amount = amount
            }
            itemStack
        }.orElse(ItemStack(Material.AIR))
    }

    @JsonGetter("reference")
    fun stackReference(): StackReference {
        return reference
    }

    /**
     * Removes the specified amount from the input ItemStack inside an inventory!
     *
     *
     * This method will directly edit the input ItemStack (Change it's type, amount, etc.) and won't return a result value!
     *
     *
     *
     * **Stackable**  ([Material.getMaxStackSize] > 1 or input ItemStack amount > 1)**:**<br></br>
     * The amount removed from the input ItemStack is equals to **`[.getAmount] * totalAmount`**
     *
     *
     * If the custom item has a replacement/craft remain:
     *
     *  * **Player is not null: **Tries to add the item/s to the players inventory. If there is no space it will drop the item at the position of the player.
     *  * **Player is null:**
     *
     *  *
     * **Location is null, Inventory is not null:** Tries to add the item/s to the inventory.<br></br>
     * If there is no space, it tries to get the location of the inventory to drop the item/s there instead.<br></br>
     * In case the inventory has no location, the item/s are not dropped and will be lost! Be careful with this!
     *
     *  * **Location is not null: **Drops the items at that location.
     *  * **Location and Inventory is null: **Item/s are neither added to an inventory or dropped.
     *
     *
     *
     *
     *
     *
     *
     * **Un-stackable**  ([Material.getMaxStackSize] == 1 and input ItemStack amount == 1)**:**<br></br>
     * This method will redirect to the [.removeUnStackableItem] method.<br></br>
     *
     *
     * <br></br>
     *
     * @param input              The input ItemStack, that is also going to be edited.
     * @param totalAmount        The amount of this custom item that should be removed from the input.
     * @param inventory          The optional inventory to add the replacements to. (Only for stackable items)
     * @param player             The player to give the items to. If the players' inventory has space the craft remains are added. (Only for stackable items)
     * @param location           The location where the replacements should be dropped. (Only for stackable items)
     * @param replaceWithRemains If the Item should be replaced by the default craft remains (Only for un-stackable items).
     */
    fun remove(
        input: ItemStack,
        totalAmount: Int,
        inventory: Inventory?,
        player: Player?,
        location: Location?,
        replaceWithRemains: Boolean
    ) {
        if (type.maxStackSize == 1 && input.amount == 1) {
            removeUnStackableItem(input, replaceWithRemains)
        } else {
            if (this.isConsumed) {
                val amount = input.amount - amount * totalAmount
                input.amount = amount
            }
            applyStackableReplacement(totalAmount, replaceWithRemains, player, inventory, location)
        }
    }

    /**
     * Removes the specified amount from the input ItemStack inside an inventory!
     *
     *
     * This method will directly edit the input ItemStack (Change it's type, amount, etc.) and won't return a result value!
     *
     *
     *
     * **Stackable**  ([Material.getMaxStackSize] > 1 or input ItemStack amount > 1)**:**<br></br>
     * The amount removed from the input ItemStack is equals to **`[.getAmount] * totalAmount`**
     *
     *
     * If the custom item has a replacement/craft remain:
     *
     *  *
     * **Location is null, Inventory is not null:** Tries to add the item/s to the inventory.<br></br>
     * If there is no space, it tries to get the location of the inventory to drop the item/s there instead.<br></br>
     * In case the inventory has no location, the item/s are not dropped and will be lost! Be careful with this!
     *
     *  * **Location is not null: **Drops the items at that location.
     *  * **Location and Inventory is null: **Item/s are neither added to an inventory or dropped.
     *
     *
     *
     *
     *
     * **Un-stackable**  ([Material.getMaxStackSize] == 1 and input ItemStack amount == 1)**:**<br></br>
     * This method will redirect to the [.removeUnStackableItem] method.<br></br>
     *
     *
     * <br></br>
     *
     * @param input              The input ItemStack, that is also going to be edited.
     * @param totalAmount        The amount of this custom item that should be removed from the input.
     * @param inventory          The optional inventory to add the replacements to. (Only for stackable items)
     * @param location           The location where the replacements should be dropped. (Only for stackable items)
     * @param replaceWithRemains If the Item should be replaced by the default craft remains (Only for un-stackable items).
     */
    /**
     * Removes the specified amount from the input ItemStack inside an inventory!
     *
     *
     * This method will directly edit the input ItemStack (Change it's type, amount, etc.) and won't return a result value!
     *
     *
     *
     * **Stackable**  ([Material.getMaxStackSize] > 1 or input ItemStack amount > 1)**:**<br></br>
     * The amount removed from the input ItemStack is equals to **`[.getAmount] * totalAmount`**
     *
     *
     * If the custom item has a replacement/craft remain:
     *
     *  *
     * **Location is null, Inventory is not null:** Tries to add the item/s to the inventory.<br></br>
     * If there is no space, it tries to get the location of the inventory to drop the item/s there instead.<br></br>
     * In case the inventory has no location, the item/s are not dropped and will be lost! Be careful with this!
     *
     *  * **Location is not null: **Drops the items at that location.
     *  * **Location and Inventory is null: **Item/s are neither added to an inventory or dropped.
     *
     *
     *
     *
     *
     * **Un-stackable**  ([Material.getMaxStackSize] == 1 and input ItemStack amount == 1)**:**<br></br>
     * This method will redirect to the [.removeUnStackableItem] method.<br></br>
     *
     *
     * <br></br>
     *
     * @param input       The input ItemStack, that is also going to be edited.
     * @param totalAmount The amount of this custom item that should be removed from the input.
     * @param inventory   The optional inventory to add the replacements to. (Only for stackable items)
     * @param location    The location where the replacements should be dropped. (Only for stackable items)
     * @see .remove
     */
    /**
     * Removes the specified amount from the input ItemStack inside an inventory!
     *
     *
     * This method will directly edit the input ItemStack (Change it's type, amount, etc.) and won't return a result value!
     *
     *
     *
     * **Stackable**  ([Material.getMaxStackSize] > 1 or input ItemStack amount > 1)**:**<br></br>
     * The amount removed from the input ItemStack is equals to **`[.getAmount] * totalAmount`**
     *
     *
     * If the custom item has a replacement/craft remain:
     *
     *  *
     * **Inventory is not null:** Tries to add the item/s to the inventory.<br></br>
     * If there is no space, it tries to get the location of the inventory to drop the item/s there instead.<br></br>
     * In case the inventory has no location, the item/s are not dropped and will be lost! Be careful with this!
     *
     *  * **Inventory is null: **Item/s are neither added to an inventory or dropped.
     *
     *
     *
     *
     *
     * **Un-stackable**  ([Material.getMaxStackSize] == 1 and input ItemStack amount == 1)**:**<br></br>
     * This method will redirect to the [.removeUnStackableItem] method.<br></br>
     *
     *
     * <br></br>
     *
     * @param input       The input ItemStack, that is also going to be edited.
     * @param totalAmount The amount of this custom item that should be removed from the input.
     * @param inventory   The optional inventory to add the replacements to. (Only for stackable items)
     * @see .remove
     */
    @JvmOverloads
    fun remove(
        input: ItemStack,
        totalAmount: Int,
        inventory: Inventory?,
        location: Location? = null,
        replaceWithRemains: Boolean = true
    ) {
        remove(input, totalAmount, inventory, null, location, replaceWithRemains)
    }

    /**
     * Removes the specified amount from the input ItemStack inside a inventory!
     *
     *
     * This method will directly edit the input ItemStack and won't return a result value.
     *
     *
     *
     * **Stackable:**<br></br>
     * The amount removed from the input ItemStack is equals to **`[.getAmount] * totalAmount`**
     *
     *
     * If the custom item has a replacement:
     *
     *  * **If location is not null,** then it will drop the items at that location.
     *  * **If location is null,** then the replacement items are neither dropped nor added to the inventory!
     *
     *
     *
     *
     *
     * **Un-stackable:**<br></br>
     * This method will redirect to the [.removeUnStackableItem] method and replaces the item with it's craft remains if available.
     *
     *
     * <br></br>
     *
     * @param input       The input ItemStack, that is also going to be edited.
     * @param totalAmount The amount of this custom item that should be removed from the input.
     * @param location    The location where the replacements should be dropped. (Only for stackable items)
     * @return The original input [ItemStack] that was directly edited by the method.
     * @see .remove
     */
    fun remove(input: ItemStack, totalAmount: Int, location: Location?): ItemStack {
        remove(input, totalAmount, null, location)
        return input
    }

    private fun applyStackableReplacement(
        totalAmount: Int,
        replaceWithRemains: Boolean,
        player: Player?,
        inventory: Inventory?,
        location: Location?
    ) {
        var location = location
        val replacement = replacement()?.originalStack() ?: if (isConsumed && replaceWithRemains && craftRemain != null) ItemStack(craftRemain) else null
        if (replacement != null) {
            replacement.amount *= totalAmount
            if (player != null) {
                val playerInv = player.inventory
                if (InventoryUtils.hasInventorySpace(playerInv, replacement)) {
                    playerInv.addItem(replacement)
                    return
                }
                location = player.location
            }
            if (location == null) {
                if (inventory == null) return
                if (InventoryUtils.hasInventorySpace(inventory, replacement)) {
                    inventory.addItem(replacement)
                    return
                }
                location = inventory.location
            }
            if (location != null && location.world != null) {
                location.world.dropItemNaturally(location.add(0.5, 1.0, 0.5), replacement)
            }
        }
    }

    /**
     * Gets the replacement of this CustomItem.
     *
     * @return Optional containing the current replacement, or empty if unset
     */
    fun replacement(): StackReference? {
        return replacement
    }

    /**
     * Sets the new replacement of this CustomItem.
     * The reference may be null, in which case it unsets any existing replacement.
     *
     * @param replacement The new replacement, or null to unset it
     */
    fun replacement(replacement: StackReference?) {
        if (replacement != null && replacement.identifier()
                .map { stackIdentifier: StackIdentifier ->
                    !ItemUtils.isAirOrNull(
                        stackIdentifier.stack(
                            ItemCreateContext.empty(
                                amount
                            )
                        )
                    )
                }.orElse(false)
        ) {
            this.replacement = replacement
        } else {
            this.replacement = null
        }
    }

    /**
     * Removes the input as an un-stackable item.
     *
     *
     * Items that have craft remains by default will be replaced with the according [Material] <br></br>
     * Like Buckets, Potions, Stew/Soup.
     *
     *
     *
     * If this CustomItem has a custom replacement then the input will be replaced with that.
     *
     * <br></br>
     *
     * @param input              The input ItemStack, that is going to be edited.
     * @param replaceWithRemains If the item should be replaced by its remains if removed. Not including custom replacement options!
     */
    /**
     * Removes the input as an un-stackable item.
     *
     *
     * Items that have craft remains by default will be replaced with the according [Material] <br></br>
     * Like Buckets, Potions, Stew/Soup.
     *
     *
     *
     * If this CustomItem has a custom replacement then the input will be replaced with that.
     *
     * <br></br>
     *
     * @param input The input ItemStack, that is going to be edited.
     */
    @JvmOverloads
    fun removeUnStackableItem(input: ItemStack, replaceWithRemains: Boolean = true) {
        if (this.isConsumed) {
            if (craftRemain != null && replaceWithRemains) {
                input.type = craftRemain
                input.setItemMeta(Bukkit.getItemFactory().getItemMeta(craftRemain))
            } else {
                input.amount = 0
            }
        }
        replacement()?.apply {
            val replace = referencedStack()
            input.type = replace.type
            input.setItemMeta(replace.itemMeta)
            input.data = replace.data
            input.amount = replace.amount
        } ?: run {
            val itemBuilder = ItemBuilder(input)
            if (itemBuilder.hasCustomDurability()) {
                itemBuilder.setCustomDamage(itemBuilder.customDamage + this.durabilityCost)
                return@run
            }
            val itemMeta = input.itemMeta
            if (itemMeta is Damageable) {
                val damage = itemMeta.damage + this.durabilityCost
                if (damage > create().type.maxDurability) {
                    input.amount = 0
                } else {
                    itemMeta.damage = damage
                }
            }
            input.setItemMeta(itemMeta)
        }
    }

    /**
     * Shrinks the specified stack by the given amount and returns the manipulated or replaced item!
     *
     *
     *
     *
     * **Stackable**  ([Material.getMaxStackSize] > 1 or stack count > 1)**:**<br></br>
     * The stack is shrunk by the specified amount (**`[.getAmount] * totalAmount`**)
     *
     *
     * If this CustomItem has a custom replacement:<br></br>
     * This calls the stackReplacement function with the shrunken stack and this CustomItem.
     * It is meant for applying the stackable replacement items.<br></br>
     * For default behaviour see [.shrink] and [.shrinkUnstackableItem]
     *
     *
     *
     *
     * **Un-stackable**  ([Material.getMaxStackSize] == 1 and stack count == 1)**:**<br></br>
     * Redirects to [.removeUnStackableItem]<br></br>
     *
     *
     * <br></br>
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
        stackReplacement: BiFunction<CustomItem?, ItemStack?, ItemStack>
    ): ItemStack {
        var stack = stack
        if (type.maxStackSize == 1 && stack.amount == 1) {
            return shrinkUnstackableItem(stack, useRemains)
        }
        if (this.isConsumed) {
            val amount = stack.amount - amount * count
            if (amount <= 0) {
                stack = ItemStack(Material.AIR)
            } else {
                stack.amount = amount
            }
            return stackReplacement.apply(this, stack)
        }
        return stack
    }

    /**
     * Shrinks the specified stack by the given amount and returns the manipulated or replaced item!
     *
     *
     * **Stackable**  ([Material.getMaxStackSize] > 1 or stack count > 1)**:**<br></br>
     * The stack is shrunk by the specified amount (**`[.getAmount] * totalAmount`**)
     *
     *
     * If this CustomItem has a custom replacement:<br></br>
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
     * **Warning! If you do not provide a location via `player`, `inventory`, or `inventory`, then the remaining items are discarded!**<br></br>
     * For custom behaviour see [.shrink].
     *
     *
     *
     *
     *
     * **Un-stackable**  ([Material.getMaxStackSize] == 1 and stack count == 1)**:**<br></br>
     * Redirects to [.removeUnStackableItem]<br></br>
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
        return reference.shrink(stack, count, useRemains, inventory, player, location)
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
        return reference.shrinkUnstackableItem(stack, useRemains,
            { stackIdentifier, itemStack -> replacement()?.referencedStack() },
            { result: ItemStack? -> changeDurability(this, stack, result) })
    }

    private fun getCraftRemain(): Material? {
        return craftRemain(itemStack)
    }

    /**
     * @return True if this item requires permission to be used, else false.
     */
    fun hasPermission(): Boolean {
        return !permission.isEmpty()
    }

    @get:JsonGetter("data")
    @set:JsonSetter("data")
    private var dataList: List<CustomItemData>
        get() = indexedData.values.stream().toList()
        private set(data) {
            for (itemData in data) {
                addOrReplaceData(itemData)
            }
        }

    fun <T : CustomItemData> addOrReplaceData(data: T): T {
        val type = data.javaClass
        return type.cast(indexedData.put(getKeyForData(type), data))
    }

    fun addDataIfAbsent(data: CustomItemData): CustomItemData? {
        return indexedData.putIfAbsent(data.key(), data)
    }

    fun computeDataIfAbsent(id: Key, mappingFunction: Function<Key, CustomItemData>): CustomItemData {
        return indexedData.computeIfAbsent(id, mappingFunction)
    }

    fun <T : CustomItemData> computeDataIfAbsent(type: Class<T>, mappingFunction: Function<Key, T>): T {
        return type.cast(indexedData.computeIfAbsent(getKeyForData(type), mappingFunction))
    }

    fun computeDataIfPresent(
        id: Key,
        remappingFunction: BiFunction<Key, CustomItemData, CustomItemData?>
    ): CustomItemData? {
        return indexedData.computeIfPresent(id, remappingFunction)
    }

    fun <T : CustomItemData> computeDataIfPresent(type: Class<T>, remappingFunction: BiFunction<Key, T, T>): T? {
        Objects.requireNonNull(remappingFunction)
        val key = getKeyForData(type)
        val oldValue = type.cast(indexedData[key])
        if (oldValue != null) {
            val newValue: T = remappingFunction.apply(key, oldValue)
            indexedData[key] = newValue
            return newValue
        }
        return null
    }

    fun getData(id: Key): Optional<CustomItemData> {
        return Optional.ofNullable(indexedData[id])
    }

    fun <T : CustomItemData> getData(type: Class<T>): Optional<T> {
        return getData(getKeyForData(type)).map { type.cast(it) }
    }

    @JsonAlias("particles")
    @JsonSetter
    fun setParticleContent(particlesNode: ObjectNode?) {
        if (particlesNode == null) {
            this.particleContent = ParticleContent()
            return
        }
        this.particleContent = Objects.requireNonNullElse(
            objectMapper.convertValue(
                particlesNode,
                ParticleContent::class.java
            ), ParticleContent()
        )

        if (Streams.stream<String>(particlesNode.fieldNames())
                .anyMatch { s: String -> ParticleLocation.valueOf(s.uppercase()).isDeprecated }
        ) { //Old version. Conversion required.
            val playerSettings =
                particleContent.player
                    ?: ParticleContent.PlayerSettings(ScaffoldingProvider.get()) //Create or get player settings
            particlesNode.fields().forEachRemaining { entry ->
                val loc = ParticleLocation.valueOf(entry.key)
                val value = entry.value
                if (value.isObject && value.has("effect")) {
                    val animation = ScaffoldingProvider.get().registries.particleAnimations[objectMapper.convertValue(
                        value["effect"],
                        Key::class.java
                    )]
                    if (animation != null) {
                        loc.applyOldPlayerAnimation(playerSettings, animation)
                    }
                }
            }
        }
    }

    var amount: Int
        /**
         * Gets the amount of the linked ItemStack or if the custom amount
         * is bigger than 0 gets the custom amount.
         *
         * @return actual amount of CustomItem
         */
        get() = reference.amount()
        /**
         * Sets the amount of the linked item.
         *
         * @param amount The new amount of the item.
         */
        set(amount) {
            reference.setAmount(amount)
        }

    val isBlock: Boolean
        get() {
            if (type.isBlock) return true
            val identifier = reference.identifier().get()
            return identifier is ItemsAdderStackIdentifier && ScaffoldingProvider.get().compatibilityManager.plugins
                .evaluateIfAvailable("ItemsAdder", ItemsAdderIntegration::class.java) { ia ->
                    ia.getStackInstance(
                        identifier.itemId()
                    ).map { it.isBlock }.orElse(false)
                }
        }

    /**
     * Converts **Legacy** CustomItems, that can behave as a reference or saved item.
     * If the CustomItem is an actual saved item, then it returns a StackReference using the WolfyUtilsStackIdentifier.
     * Otherwise, it simply returns the reference of this CustomItem.
     *
     * @return The reference, or a reference to this item when it is a saved item.
     */
    @Deprecated("")
    fun convertToReference(): StackReference {
        if (hasNamespacedKey()) {
            return StackReference(
                ScaffoldingProvider.get(), reference.amount(), reference.weight(), CustomItemStackIdentifier(key()),
                itemStack
            )
        }
        return reference
    }

    override fun toString(): String {
        return ("CustomItem{" +
                ", namespacedKey=" + namespacedKey +
                ", craftRemain=" + craftRemain +
                ", consumed=" + isConsumed +
                ", replacement=" + replacement +
                ", durabilityCost=" + durabilityCost +
                ", permission='" + permission +
                ", fuelSettings=" + fuelSettings +
                ", blockPlacement=" + isBlockPlacement +
                ", blockVanillaEquip=" + isBlockVanillaEquip +
                ", blockVanillaRecipes=" + isBlockVanillaRecipes +
                ", equipmentSlots=" + equipmentSlots +
                ", apiReference=" + reference +
                ", particleContent=" + particleContent +
                ", metaSettings=" + nbtChecks +
                "} " + super.toString())
    }

    companion object {
        @JvmField
        val PERSISTENT_KEY_TAG: NamespacedKey = scaffoldingKey("custom_item").bukkit()

        /**
         *
         *
         * This will create a **new** [CustomItem] that wraps the specified reference.
         *
         *
         *
         *
         *
         * @param reference The reference to wrap
         * @return A new CustomItem instance that wraps the specified reference
         */
        fun wrap(key: Key, reference: StackReference?): Optional<CustomItem> {
            if (reference == null) return Optional.empty()
            return Optional.of(CustomItem(key, reference))
        }

        /**
         * Get the CustomItem via ItemStack.
         * It checks for the PersistentData containing the NamespacedKey of WolfyUtilities.
         * When that isn't found it checks for ItemsAdder and Oraxen values saved in the Items NBT.
         *
         * @param itemStack the ItemStack to check
         * @return the CustomItem linked to the specific API this Item is from.
         */
        fun getReferenceByItemStack(key: Key, itemStack: ItemStack?): CustomItem? {
            if (itemStack != null) {
                val core = ScaffoldingProvider.get()
                val reference: StackReference = StackReference(
                    core,
                    itemStack.amount,
                    1.0,
                    core.registries.stackIdentifierParsers.parseIdentifier(itemStack),
                    itemStack
                )
                return CustomItem(key, reference)
            }
            return null
        }

        /**
         * This method returns the original CustomItem from the ItemStack.
         * This only works if the itemStack contains a NamespacedKey corresponding to a CustomItem
         * that is saved!
         *
         *
         * If you need access to the original CustomItem variables use this method.
         *
         *
         * If you want to detect what plugin this ItemStack is from and use it's corresponding Reference use [.getReferenceByItemStack] instead!
         *
         * @param itemStack
         * @return CustomItem the ItemStack is linked to, only if it is saved, else returns null
         */
        @JvmStatic
        fun getByItemStack(itemStack: ItemStack?): CustomItem? {
            return ScaffoldingProvider.get().registries.customItems.getByItemStack(itemStack)
        }

        /**
         * @param itemMeta The ItemMeta to get the key from.
         * @return The CustomItems [NamespacedKey] from the ItemMeta; or null if the ItemMeta doesn't contain a key.
         */
        fun getKeyOfItemMeta(itemMeta: ItemMeta): Key? {
            val container = itemMeta.persistentDataContainer
            if (container.has(PERSISTENT_KEY_TAG, PersistentDataType.STRING)) {
                return parse(container.get(PERSISTENT_KEY_TAG, PersistentDataType.STRING)!!)
            }
            return null
        }

        @JvmStatic
        fun changeDurability(customItem: CustomItem, stack: ItemStack, result: ItemStack?): ItemStack? {
            if (customItem.durabilityCost != 0) {
                // handle custom durability
                val itemBuilder = ItemBuilder(stack)
                if (itemBuilder.hasCustomDurability()) {
                    val damage = itemBuilder.customDamage + customItem.durabilityCost
                    if (damage > itemBuilder.customDurability) {
                        return result
                    }
                    itemBuilder.setCustomDamage(damage)
                    return itemBuilder.create()
                }
                // handle vanilla durability
                val itemMeta = stack.itemMeta
                if (itemMeta is Damageable) {
                    val damage: Int = itemMeta.damage + customItem.durabilityCost
                    if (damage > customItem.type.maxDurability) {
                        return result
                    }
                    itemMeta.damage = damage
                    stack.setItemMeta(itemMeta)
                    return stack
                }
            }
            return result
        }

        @JvmStatic
        fun craftRemain(stack: ItemStack): Material? {
            if (!ItemUtils.isAirOrNull(stack) && stack.type.isItem) {
                val replaceType = stack.type.craftingRemainingItem
                if (replaceType != null) return replaceType
                return when (stack.type.name) {
                    "LAVA_BUCKET", "MILK_BUCKET", "WATER_BUCKET", "COD_BUCKET", "SALMON_BUCKET", "PUFFERFISH_BUCKET", "TROPICAL_FISH_BUCKET" -> Material.BUCKET
                    "POTION" -> Material.GLASS_BOTTLE
                    "BEETROOT_SOUP", "MUSHROOM_STEW", "RABBIT_STEW" -> Material.BOWL
                    else -> null
                }
            }
            return null
        }

        private fun getKeyForData(type: Class<out CustomItemData>): Key {
            return ScaffoldingProvider.get().registries.customItemData.getKey(type)
                ?: throw IllegalArgumentException("Failed to find Custom Item Data for type $type")
        }
    }
}