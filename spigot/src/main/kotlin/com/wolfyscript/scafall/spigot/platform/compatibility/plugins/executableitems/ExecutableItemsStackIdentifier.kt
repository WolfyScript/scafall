package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.executableitems

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.ssomar.score.api.executableitems.ExecutableItemsAPI
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface
import com.ssomar.score.api.executableitems.config.ExecutableItemsManagerInterface
import com.wolfyscript.scafall.dependency.DependencyResolverSettings
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationDependencyResolver
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationDependencyResolverSettings
import com.wolfyscript.scafall.spigot.platform.world.items.ItemUtils
import com.wolfyscript.scafall.spigot.platform.world.items.reference.ItemCreateContext
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifier
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifierParser
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@StaticNamespacedKey(key = "executableitems")
@DependencyResolverSettings(PluginIntegrationDependencyResolver::class)
@PluginIntegrationDependencyResolverSettings(
    pluginName = ExecutableItemsIntegration.PLUGIN_NAME,
    integration = ExecutableItemsIntegration::class
)
class ExecutableItemsStackIdentifier : StackIdentifier {
    @JsonIgnore
    private val manager: ExecutableItemsManagerInterface

    @get:JsonGetter("id")
    val id: String

    @JsonCreator
    constructor(@JsonProperty("id") id: String) {
        this.manager = ExecutableItemsAPI.getExecutableItemsManager()
        this.id = id
    }

    constructor(manager: ExecutableItemsManagerInterface, id: String) {
        this.id = id
        this.manager = manager
    }

    private constructor(other: ExecutableItemsStackIdentifier) {
        this.id = other.id
        this.manager = other.manager
    }

    override fun stack(context: ItemCreateContext): ItemStack {
        return manager.getExecutableItem(id)
            .map { item: ExecutableItemInterface ->
                item.buildItem(
                    context.amount(),
                    context.player()
                )
            }.orElseGet { ItemStack(Material.AIR) }
    }

    override fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean {
        if (ItemUtils.isAirOrNull(other)) return false
        return manager.getExecutableItem(other)
            .map { exeItem: ExecutableItemInterface -> exeItem.id == id }
            .orElse(false)
    }

    override fun key(): Key = ID

    class Parser(private val manager: ExecutableItemsManagerInterface) : StackIdentifierParser<ExecutableItemsStackIdentifier> {

        override fun priority(): Int {
            return 1800
        }

        override fun from(itemStack: ItemStack?): ExecutableItemsStackIdentifier {
            return manager.getExecutableItem(itemStack)
                .map { ExecutableItemsStackIdentifier(manager, it.id) }.orElse(null)
        }

        override fun key(): Key = ID

    }

    companion object {
        val ID: Key = Key.scaffoldingKey("executableitems")
    }
}
