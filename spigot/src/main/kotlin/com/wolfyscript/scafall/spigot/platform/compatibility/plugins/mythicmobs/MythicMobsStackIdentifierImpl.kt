package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.mythicmobs

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.dependency.DependencyResolverSettings
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationDependencyResolver
import com.wolfyscript.scafall.spigot.platform.compatibility.PluginIntegrationDependencyResolverSettings
import com.wolfyscript.scafall.spigot.platform.world.items.ItemUtils.isAirOrNull
import com.wolfyscript.scafall.spigot.platform.world.items.reference.ItemCreateContext
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifier
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifierParser
import de.tr7zw.changeme.nbtapi.NBTItem
import io.lumine.mythic.bukkit.MythicBukkit
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@StaticNamespacedKey(key = "mythicmobs")
@DependencyResolverSettings(
    PluginIntegrationDependencyResolver::class
)
@PluginIntegrationDependencyResolverSettings(
    pluginName = MythicMobsIntegration.KEY,
    integration = MythicMobsIntegration::class
)
class MythicMobsStackIdentifierImpl @JsonCreator constructor(@get:JsonGetter("item") @param:JsonProperty("item") val itemName: String) : StackIdentifier, MythicMobsStackIdentifier {

    @JsonIgnore
    private val mythicBukkit: MythicBukkit = MythicBukkit.inst()

    override fun stack(context: ItemCreateContext): ItemStack {
        val stack = mythicBukkit.itemManager.getItemStack(itemName)
        if (stack != null) {
            stack.amount = context.amount()
        }
        return stack
    }

    override fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean {
        if (isAirOrNull(other)) return false
        val tag = NBTItem.convertItemtoNBT(other).getCompound("tag") ?: return false
        if (tag.hasTag(ITEM_KEY)) {
            return this.itemName == tag.getString(ITEM_KEY)
        }
        return false
    }

    override fun key(): Key {
        return ID
    }

    class Parser : StackIdentifierParser<MythicMobsStackIdentifier> {
        override fun priority(): Int {
            return 1600
        }

        override fun from(itemStack: ItemStack?): MythicMobsStackIdentifier? {
            if (isAirOrNull(itemStack)) return null
            val tag = NBTItem.convertItemtoNBT(itemStack).getCompound("tag") ?: return null
            if (tag.hasTag(ITEM_KEY)) {
                return MythicMobsStackIdentifierImpl(tag.getString(ITEM_KEY))
            }
            return null
        }

        override fun key(): Key {
            return ID
        }

        override fun displayConfig(): StackIdentifierParser.DisplayConfiguration {
            return StackIdentifierParser.DisplayConfiguration.SimpleDisplayConfig(
                Component.text("MythicMobs").color(NamedTextColor.DARK_PURPLE).decorate(TextDecoration.BOLD),
                StackIdentifierParser.DisplayConfiguration.MaterialIconSettings(Material.WITHER_SKELETON_SKULL)
            )
        }
    }

    companion object {
        protected const val ITEM_KEY: String = "MYTHIC_TYPE"
        val ID: Key = scaffoldingKey("mythicmobs")
    }
}
