package com.wolfyscript.scafall.spigot.platform.compatibility.plugins.fancybags

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
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
import de.tr7zw.changeme.nbtapi.NBTType
import me.chickenstyle.backpack.Utils
import me.chickenstyle.backpack.configs.CustomBackpacks
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@StaticNamespacedKey(key = "fancybags")
@DependencyResolverSettings(
    PluginIntegrationDependencyResolver::class
)
@PluginIntegrationDependencyResolverSettings(pluginName = FancyBagsImpl.KEY, integration = FancyBagsImpl::class)
class FancyBagsStackIdentifier @JsonCreator constructor(
    @param:JsonProperty(
        "id"
    ) private val id: Int
) :
    StackIdentifier {
    @JsonGetter("id")
    fun id(): Int {
        return id
    }

    override fun stack(context: ItemCreateContext): ItemStack {
        val bag = CustomBackpacks.getBackpack(id)
        if (bag != null) {
            val stack = Utils.createBackpackItemStack(bag.name, bag.texture, bag.slotsAmount, bag.id)
            stack.amount = context.amount()
            return stack
        }
        return ItemStack(Material.AIR)
    }

    override fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean {
        val nbtItem = NBTItem(other)
        if (nbtItem.hasTag(ID_TAG, NBTType.NBTTagInt)) {
            return nbtItem.getInteger(ID_TAG) == id
        }
        return false
    }

    override fun key(): Key {
        return ID
    }

    class Parser :
        StackIdentifierParser<FancyBagsStackIdentifier> {
        override fun priority(): Int {
            return 0
        }

        override fun from(itemStack: ItemStack?): FancyBagsStackIdentifier? {
            if (isAirOrNull(itemStack)) return null
            val nbtItem = NBTItem(itemStack)
            if (nbtItem.hasTag(ID_TAG, NBTType.NBTTagInt)) {
                return FancyBagsStackIdentifier(nbtItem.getInteger(ID_TAG))
            }
            return null
        }

        override fun key(): Key {
            return ID
        }
    }

    companion object {
        private const val ID_TAG = "BackpackID"
        val ID: Key = scaffoldingKey("fancybags")
    }
}
