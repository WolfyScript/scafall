package com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.oraxen

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scaffolding.dependency.DependencyResolverSettings
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import com.wolfyscript.scaffolding.spigot.platform.compatibility.PluginIntegrationDependencyResolver
import com.wolfyscript.scaffolding.spigot.platform.compatibility.PluginIntegrationDependencyResolverSettings
import com.wolfyscript.scaffolding.spigot.platform.world.items.ItemUtils
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.ItemCreateContext
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifier
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifierParser
import io.th0rgal.oraxen.api.OraxenItems
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@StaticNamespacedKey(key = "oraxen")
@DependencyResolverSettings(PluginIntegrationDependencyResolver::class)
@PluginIntegrationDependencyResolverSettings(pluginName = OraxenIntegration.KEY, integration = OraxenIntegration::class)
class OraxenStackIdentifierImpl @JsonCreator constructor(@param:JsonProperty("item") private val itemID: String) : StackIdentifier {

    @JsonGetter("item")
    fun itemId(): String {
        return itemID
    }

    override fun stack(context: ItemCreateContext): ItemStack {
        if (OraxenItems.exists(itemID)) {
            val stack = OraxenItems.getItemById(itemID).build()
            stack.amount = context.amount()
            return stack
        }
        return ItemUtils.AIR
    }

    override fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean {
        val itemId = OraxenItems.getIdByItem(other)
        if (itemId != null && itemId.isNotEmpty()) {
            return this.itemID == itemId
        }
        return false
    }

    override fun key(): Key {
        return ID
    }

    class Parser :
        StackIdentifierParser<OraxenStackIdentifierImpl> {
        override fun priority(): Int {
            return 1900
        }

        override fun from(itemStack: ItemStack?): OraxenStackIdentifierImpl? {
            val itemId = OraxenItems.getIdByItem(itemStack)
            if (itemId != null && !itemId.isEmpty()) {
                return OraxenStackIdentifierImpl(itemId)
            }
            return null
        }

        override fun key(): Key {
            return ID
        }

        override fun displayConfig(): StackIdentifierParser.DisplayConfiguration {
            return StackIdentifierParser.DisplayConfiguration.SimpleDisplayConfig(
                Component.text("Oraxen").color(NamedTextColor.DARK_AQUA).decorate(TextDecoration.BOLD),
                StackIdentifierParser.DisplayConfiguration.MaterialIconSettings(Material.DIAMOND)
            )
        }
    }

    companion object {
        val ID: Key = scaffoldingKey("oraxen")
    }
}
