package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.mmoitems

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.dependency.DependencyResolverSettings
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.defaultKey
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationDependencyResolver
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationDependencyResolverSettings
import com.wolfyscript.scafall.spigot.platform.world.items.ItemUtils.isAirOrNull
import com.wolfyscript.scafall.spigot.platform.world.items.reference.ItemCreateContext
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifier
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifierParser
import io.lumine.mythic.lib.api.item.NBTItem
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.api.Type
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@StaticNamespacedKey(key = "mmoitems")
@DependencyResolverSettings(
    PluginIntegrationDependencyResolver::class
)
@PluginIntegrationDependencyResolverSettings(pluginName = MMOItemsImpl.PLUGIN_NAME, integration = MMOItemsImpl::class)
class MMOItemsStackIdentifierImpl : StackIdentifier {
    @get:JsonIgnore
    val itemType: Type?

    @get:JsonGetter("name")
    val itemName: String

    constructor(itemType: Type?, itemName: String) {
        this.itemType = itemType
        this.itemName = itemName
    }

    @JsonCreator
    internal constructor(@JsonProperty("type") itemTypeId: String?, @JsonProperty("name") itemName: String) {
        this.itemType = MMOItems.plugin.types[itemTypeId]
        this.itemName = itemName
    }

    @get:JsonGetter("type")
    private val typeId: String
        get() = itemType!!.id

    override fun stack(context: ItemCreateContext): ItemStack {
        val item = MMOItems.plugin.getMMOItem(itemType, itemName) ?: return ItemStack(Material.AIR)
        val stack = item.newBuilder().buildSilently()
        stack.amount = context.amount()
        return stack
    }

    override fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean {
        if (isAirOrNull(other)) return false
        val nbtItem = NBTItem.get(other)
        if (!nbtItem.hasType()) return false
        return this.itemType == MMOItems.plugin.types[nbtItem.type] && this.itemName == nbtItem.getString("MMOITEMS_ITEM_ID")
    }

    override fun key(): Key {
        return ID
    }

    class Parser : StackIdentifierParser<MMOItemsStackIdentifierImpl> {

        override fun priority(): Int {
            return 2000
        }

        override fun from(itemStack: ItemStack?): MMOItemsStackIdentifierImpl? {
            if (isAirOrNull(itemStack)) return null
            val nbtItem = NBTItem.get(itemStack)
            if (nbtItem.hasType()) {
                val type = MMOItems.plugin.types[nbtItem.type]
                val itemId = nbtItem.getString("MMOITEMS_ITEM_ID")
                return MMOItemsStackIdentifierImpl(type, itemId)
            }
            return null
        }

        override fun key(): Key {
            return ID
        }

        override fun displayConfig(): StackIdentifierParser.DisplayConfiguration {
            return StackIdentifierParser.DisplayConfiguration.SimpleDisplayConfig(
                Component.text("MMOItems").color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.BOLD),
                StackIdentifierParser.DisplayConfiguration.MaterialIconSettings(Material.IRON_SWORD)
            )
        }
    }

    companion object {
        val ID: Key = defaultKey("mmoitems")
    }
}
