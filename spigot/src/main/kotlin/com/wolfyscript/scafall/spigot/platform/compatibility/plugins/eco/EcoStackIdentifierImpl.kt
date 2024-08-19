package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.eco

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.willfp.eco.core.items.Items
import com.wolfyscript.scafall.dependency.DependencyResolverSettings
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationDependencyResolver
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationDependencyResolverSettings
import com.wolfyscript.scafall.spigot.platform.world.items.ItemUtils
import com.wolfyscript.scafall.spigot.platform.world.items.reference.ItemCreateContext
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifier
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifierParser
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

@StaticNamespacedKey(key = "eco")
@DependencyResolverSettings(
    PluginIntegrationDependencyResolver::class
)
@PluginIntegrationDependencyResolverSettings(pluginName = EcoIntegration.KEY, integration = EcoIntegration::class)
class EcoStackIdentifierImpl @JsonCreator constructor(
    @get:JsonGetter("itemKey") @param:JsonProperty("itemKey") override val itemKey: NamespacedKey
) : StackIdentifier, EcoStackIdentifier {

    override fun stack(context: ItemCreateContext): ItemStack {
        val stack = Items.lookup(itemKey.toString()).item
        if (stack != null) {
            stack.amount = context.amount()
        }
        return stack
    }

    override fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean {
        if (ItemUtils.isAirOrNull(other)) return false
        val item = Items.getCustomItem(other!!)
        return item != null && itemKey == item.key
    }

    override fun key(): Key = ID

    class Parser : StackIdentifierParser<EcoStackIdentifierImpl> {
        override fun priority(): Int {
            return 100
        }

        override fun from(itemStack: ItemStack?): EcoStackIdentifierImpl? {
            if (itemStack?.let { Items.isCustomItem(it) } == true) {
                val customStack = Items.getCustomItem(itemStack)
                if (customStack != null && !customStack.key.key.startsWith("wrapped_")) { // Ignore wrapped items as those may be linked to another plugin?! like ItemsAdder
                    return EcoStackIdentifierImpl(customStack.key)
                }
            }
            return null
        }

        override fun key(): Key = ID

        override fun displayConfig(): StackIdentifierParser.DisplayConfiguration {
            return StackIdentifierParser.DisplayConfiguration.SimpleDisplayConfig(
                Component.text("Eco").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD),
                StackIdentifierParser.DisplayConfiguration.MaterialIconSettings(Material.EMERALD)
            )
        }

    }

    companion object {
        val ID: Key = Key.defaultKey("eco")
    }
}
