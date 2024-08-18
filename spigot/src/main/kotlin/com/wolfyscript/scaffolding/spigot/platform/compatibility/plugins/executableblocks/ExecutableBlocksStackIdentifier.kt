package com.wolfyscript.scaffolding.spigot.platform.compatibility.plugins.executableblocks

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.ssomar.executableblocks.executableblocks.ExecutableBlock
import com.ssomar.executableblocks.executableblocks.ExecutableBlocksManager
import com.wolfyscript.scaffolding.ScaffoldingProvider
import com.wolfyscript.scaffolding.dependency.DependencyResolverSettings
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import com.wolfyscript.scaffolding.spigot.api.compatibilityManager
import com.wolfyscript.scaffolding.spigot.platform.compatibility.PluginIntegrationDependencyResolver
import com.wolfyscript.scaffolding.spigot.platform.compatibility.PluginIntegrationDependencyResolverSettings
import com.wolfyscript.scaffolding.spigot.platform.world.items.ItemUtils
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.ItemCreateContext
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifier
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifierParser
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@StaticNamespacedKey(key = "executableblocks")
@DependencyResolverSettings(PluginIntegrationDependencyResolver::class)
@PluginIntegrationDependencyResolverSettings(pluginName = ExecutableBlocksIntegration.PLUGIN_NAME, integration = ExecutableBlocksIntegration::class)
class ExecutableBlocksStackIdentifier : StackIdentifier {
    private val integration: ExecutableBlocksIntegration
    private val manager: ExecutableBlocksManager

    @get:JsonGetter("id")
    val id: String

    @JsonCreator
    constructor(@JsonProperty("id") id: String) {
        this.integration = ScaffoldingProvider.get().compatibilityManager.plugins.getIntegration("ExecutableBlocks", ExecutableBlocksIntegration::class.java)!!
        this.manager = ExecutableBlocksManager.getInstance()
        this.id = id
    }

    constructor(integration: ExecutableBlocksIntegration, manager: ExecutableBlocksManager, id: String) {
        this.id = id
        this.manager = manager
        this.integration = integration
    }

    private constructor(other: ExecutableBlocksStackIdentifier) {
        this.id = other.id
        this.manager = other.manager
        this.integration = other.integration
    }

    override fun stack(context: ItemCreateContext): ItemStack {
        return manager.getExecutableBlock(id)
            .map { eb: ExecutableBlock ->
                eb.buildItem(context.amount(), context.player())
            }.orElseGet { ItemStack(Material.AIR) }
    }

    override fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean {
        if (ItemUtils.isAirOrNull(other)) return false
        return integration.getExecutableBlock(other)?.let { eB -> eB == id } ?: false
    }

    override fun key(): Key = ID

    class Parser(private val integration: ExecutableBlocksIntegration, private val manager: ExecutableBlocksManager) : StackIdentifierParser<ExecutableBlocksStackIdentifier> {

        override fun priority(): Int {
            return 1700
        }

        override fun from(itemStack: ItemStack?): ExecutableBlocksStackIdentifier? {
            return integration.getExecutableBlock(itemStack)?.let { ExecutableBlocksStackIdentifier(integration, manager, it) }
        }

        override fun key(): Key = ID

    }

    companion object {
        val ID: Key = Key.scaffoldingKey("executableblocks")
    }
}
