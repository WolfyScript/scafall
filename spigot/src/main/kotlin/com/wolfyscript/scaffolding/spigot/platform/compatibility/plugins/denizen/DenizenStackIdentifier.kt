package com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.denizen

import com.denizenscript.denizen.scripts.containers.core.ItemScriptHelper
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.wolfyscript.scaffolding.ScaffoldingProvider
import com.wolfyscript.scaffolding.config.jackson.JacksonUtil
import com.wolfyscript.scaffolding.dependency.DependencyResolverSettings
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import com.wolfyscript.scaffolding.spigot.platform.compatibility.PluginIntegrationDependencyResolver
import com.wolfyscript.scaffolding.spigot.platform.compatibility.PluginIntegrationDependencyResolverSettings
import com.wolfyscript.scaffolding.spigot.platform.world.items.ItemUtils
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.ItemCreateContext
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifier
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifierParser
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

@StaticNamespacedKey(key = "denizen")
@DependencyResolverSettings(PluginIntegrationDependencyResolver::class)
@PluginIntegrationDependencyResolverSettings(
    pluginName = DenizenIntegrationImpl.Companion.PLUGIN_NAME,
    integration = DenizenIntegrationImpl::class
)
class DenizenStackIdentifier @JsonCreator constructor(
    @get:JsonGetter("displayItem") @param:JsonProperty(
        "displayItem"
    ) val displayItem: ItemStack, @get:JsonGetter("script") @param:JsonProperty(
        "script"
    ) val itemScript: String
) :
    StackIdentifier {
    override fun stack(context: ItemCreateContext): ItemStack {
        val stack = displayItem.clone()
        stack.amount = context.amount()
        return stack
    }

    override fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean {
        return ItemScriptHelper.getItemScriptNameText(other) == itemScript
    }

    override fun key(): Key = ID

    class Parser : StackIdentifierParser<DenizenStackIdentifier> {
        override fun priority(): Int {
            return 0
        }

        override fun from(itemStack: ItemStack?): DenizenStackIdentifier? {
            val script: String = ItemScriptHelper.getItemScriptNameText(itemStack)
            if (script != null && itemStack != null) {
                return DenizenStackIdentifier(itemStack, script)
            }
            return null
        }

        override fun key() : Key = ID

        override fun displayConfig(): StackIdentifierParser.DisplayConfiguration {
            return StackIdentifierParser.DisplayConfiguration.SimpleDisplayConfig(
                Component.text("Denizen").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD),
                StackIdentifierParser.DisplayConfiguration.MaterialIconSettings(Material.COMMAND_BLOCK)
            )
        }

    }

    companion object {
        val ID: Key = Key.scaffoldingKey("denizen")
    }
}
