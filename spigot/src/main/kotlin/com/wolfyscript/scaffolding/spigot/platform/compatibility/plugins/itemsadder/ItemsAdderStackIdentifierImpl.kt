package com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.itemsadder

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
import dev.lone.itemsadder.api.CustomStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@StaticNamespacedKey(key = "itemsadder")
@DependencyResolverSettings(
    PluginIntegrationDependencyResolver::class
)
@PluginIntegrationDependencyResolverSettings(pluginName = ItemsAdderIntegration.KEY, integration = ItemsAdderImpl::class)
class ItemsAdderStackIdentifierImpl @JsonCreator constructor(
    @param:JsonProperty(
        "id"
    ) private val itemId: String
) :
    StackIdentifier {
    @JsonGetter("id")
    fun itemId(): String {
        return itemId
    }

    override fun stack(context: ItemCreateContext): ItemStack {
        val customStack = CustomStack.getInstance(itemId)
        if (customStack != null) {
            val stack = customStack.itemStack
            stack.amount = context.amount()
            return stack
        }
        return ItemUtils.AIR
    }

    override fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean {
        val customStack = CustomStack.byItemStack(other)
        return customStack != null && itemId == customStack.namespacedID
    }

    override fun key(): Key {
        return ID
    }

    class Parser :
        StackIdentifierParser<ItemsAdderStackIdentifierImpl> {
        override fun priority(): Int {
            return 1500
        }

        override fun from(itemStack: ItemStack?): ItemsAdderStackIdentifierImpl? {
            val customStack = CustomStack.byItemStack(itemStack)
            if (customStack != null) {
                return ItemsAdderStackIdentifierImpl(customStack.namespacedID)
            }
            return null
        }

        override fun key(): Key {
            return ID
        }

        override fun displayConfig(): StackIdentifierParser.DisplayConfiguration {
            return StackIdentifierParser.DisplayConfiguration.SimpleDisplayConfig(
                Component.text("ItemsAdder").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD),
                StackIdentifierParser.DisplayConfiguration.MaterialIconSettings(Material.GRASS_BLOCK)
            )
        }
    }

    companion object {
        val ID: Key = scaffoldingKey("itemsadder")
    }
}
