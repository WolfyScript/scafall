package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.magic

import com.elmakers.mine.bukkit.api.magic.MagicAPI
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.base.Preconditions
import com.wolfyscript.scafall.dependency.DependencyResolverSettings
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.defaultKey
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationDependencyResolver
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationDependencyResolverSettings
import com.wolfyscript.scafall.spigot.platform.world.items.ItemUtils
import com.wolfyscript.scafall.spigot.platform.world.items.reference.ItemCreateContext
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifier
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifierParser
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

@StaticNamespacedKey(key = "magic")
@DependencyResolverSettings(
    PluginIntegrationDependencyResolver::class
)
@PluginIntegrationDependencyResolverSettings(pluginName = MagicImpl.PLUGIN_NAME, integration = MagicImpl::class)
class MagicStackIdentifierImpl(magicAPI: MagicAPI, itemKey: String) : StackIdentifier {
    @get:JsonGetter("itemKey")
    val itemKey: String

    @JsonIgnore
    private val magicAPI: MagicAPI

    @JsonCreator
    constructor(@JsonProperty("itemKey") itemKey: String) : this(getMagicAPI(), itemKey)

    init {
        Preconditions.checkNotNull(magicAPI, "No MagicAPI specified when creating a MagicStackIdentifier")
        this.magicAPI = magicAPI
        this.itemKey = itemKey
    }

    override fun stack(context: ItemCreateContext): ItemStack {
        val stack = Objects.requireNonNullElse(magicAPI.controller.createItem(itemKey), ItemUtils.AIR)
        stack?.amount = context.amount()
        return stack ?: ItemStack(Material.AIR)
    }

    override fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean {
        return magicAPI.controller.getItemKey(other) == itemKey
    }

    override fun key(): Key {
        return ID
    }

    class Parser(private val magicAPI: MagicAPI) :
        StackIdentifierParser<MagicStackIdentifierImpl> {
        override fun priority(): Int {
            return 600
        }

        override fun from(itemStack: ItemStack?): MagicStackIdentifierImpl? {
            if (magicAPI.isBrush(itemStack) || magicAPI.isSpell(itemStack) || magicAPI.isUpgrade(itemStack) || magicAPI.isWand(
                    itemStack
                )
            ) {
                return MagicStackIdentifierImpl(magicAPI.getItemKey(itemStack))
            }
            return null
        }

        override fun key(): Key {
            return ID
        }
    }

    companion object {
        val ID: Key = defaultKey("magic")

        fun getMagicAPI() : MagicAPI {
            val plugin = Bukkit.getPluginManager().getPlugin("Magic")
            if (plugin is MagicAPI) {
                return plugin
            }
            throw RuntimeException("Failed to find the MagicAPI")
        }
    }
}
