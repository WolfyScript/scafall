package com.wolfyscript.scafall.spigot.platform.world.items.reference

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scafall.identifier.StaticNamespacedKey
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifierParser.DisplayConfiguration
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifierParser.DisplayConfiguration.MaterialIconSettings
import com.wolfyscript.scafall.spigot.platform.world.items.reference.StackIdentifierParser.DisplayConfiguration.SimpleDisplayConfig
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

@StaticNamespacedKey(key = "bukkit")
class BukkitStackIdentifier @JsonCreator constructor(
    @param:JsonProperty(
        "stack"
    ) private val stack: ItemStack
) :
    StackIdentifier {
    @JsonGetter("stack")
    fun stack(): ItemStack {
        return stack
    }

    override fun stack(context: ItemCreateContext): ItemStack {
        val cloned = stack.clone()
        cloned.amount = context.amount()
        return cloned
    }

    override fun matchesIgnoreCount(other: ItemStack?, exact: Boolean): Boolean {
        if (other == null) return false
        if (stack.type != other.type) return false
        if (stack.hasItemMeta() || exact) return stack.isSimilar(other)
        return true
    }

    override fun key(): Key {
        return ID
    }

    class Parser : StackIdentifierParser<BukkitStackIdentifier> {
        override fun priority(): Int {
            return -2048
        }

        override fun from(itemStack: ItemStack?): BukkitStackIdentifier {
            if (itemStack == null) return BukkitStackIdentifier(ItemStack(Material.AIR))
            val copy = itemStack.clone()
            copy.amount = 1 // The identifiers should only have a stack of 1, the amount is handled by the StackReference
            return BukkitStackIdentifier(copy)
        }

        override fun key(): Key {
            return ID
        }

        override fun displayConfig(): DisplayConfiguration {
            return SimpleDisplayConfig(
                Component.text("Bukkit").color(NamedTextColor.WHITE).decorate(TextDecoration.BOLD),
                MaterialIconSettings(Material.LAVA_BUCKET)
            )
        }
    }

    companion object {
        val ID: Key = scaffoldingKey("bukkit")
    }
}
