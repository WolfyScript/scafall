package com.wolfyscript.scaffolding.spigot.platform.world.items.reference

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scaffolding.ScaffoldingProvider.Companion.get
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import com.wolfyscript.scaffolding.spigot.api.identifiers.bukkit
import com.wolfyscript.scaffolding.spigot.platform.customItems
import com.wolfyscript.scaffolding.spigot.platform.world.items.CustomItem
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifierParser.DisplayConfiguration
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifierParser.DisplayConfiguration.MaterialIconSettings
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifierParser.DisplayConfiguration.SimpleDisplayConfig
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

@StaticNamespacedKey(key = "wolfyutils")
class CustomItemStackIdentifier @JsonCreator constructor(
    @param:JsonProperty(
        "item"
    ) private val itemKey: Key
) :
    StackIdentifier {
    @JsonGetter("item")
    fun itemKey(): Key {
        return itemKey
    }

    /**
     * Gets the stack this identifier references.
     * It uses the [CustomItem.create] method to create the stack, or returns null if the referenced [CustomItem] is unavailable.
     *
     * @return The referenced ItemStack or null if referenced [CustomItem] is unavailable
     */
    override fun stack(context: ItemCreateContext): ItemStack {
        return customItem()?.create(context.amount()) ?: run {
            get().logger.warn("Couldn't find CustomItem for {}", itemKey.toString())
            ItemStack(Material.AIR)
        }
    }

    /**
     * Gets the [CustomItem] this identifier references.
     *
     * @return The referenced [CustomItem] of this identifier
     */
    fun customItem(): CustomItem? {
        return get().registries.customItems[itemKey]
    }

    override fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean {
        if (other == null) return false
        val itemMeta = other.itemMeta ?: return false
        val container = itemMeta.persistentDataContainer
        if (container.has(CUSTOM_ITEM_KEY, PersistentDataType.STRING)) {
            return this.itemKey == Key.parse(container.get(CUSTOM_ITEM_KEY, PersistentDataType.STRING)!!)
        }
        return false
    }

    override fun key(): Key = ID

    class Parser : StackIdentifierParser<CustomItemStackIdentifier> {
        override fun priority(): Int {
            return 0
        }

        override fun from(itemStack: ItemStack?): CustomItemStackIdentifier? {
            if (itemStack == null) return null
            val itemMeta = itemStack.itemMeta
            if (itemMeta != null) {
                val container = itemMeta.persistentDataContainer
                if (container.has(CUSTOM_ITEM_KEY, PersistentDataType.STRING)) {
                    return CustomItemStackIdentifier(
                            Key.parse(
                                container.get(
                                    CUSTOM_ITEM_KEY, PersistentDataType.STRING
                                )!!
                            )
                        )
                }
            }
            return null
        }

        override fun key(): Key = ID

        override fun displayConfig(): DisplayConfiguration {
            return SimpleDisplayConfig(
                Component.text("WolfyUtils").color(NamedTextColor.DARK_AQUA).decorate(TextDecoration.BOLD),
                MaterialIconSettings(Material.CRAFTING_TABLE)
            )
        }
    }


    companion object {
        private val CUSTOM_ITEM_KEY: NamespacedKey = scaffoldingKey("custom_item").bukkit()
        val ID: Key = scaffoldingKey("wolfyutils")
    }
}
