package com.wolfyscript.scaffolding.spigot.platform.world.items.reference

import com.wolfyscript.scaffolding.identifier.Keyed
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifierParser.DisplayConfiguration.MaterialIconSettings
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifierParser.DisplayConfiguration.SimpleDisplayConfig
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface StackIdentifierParser<T : StackIdentifier> : Keyed, Comparable<StackIdentifierParser<*>> {
    fun priority(): Int

    fun displayConfig(): DisplayConfiguration {
        return SimpleDisplayConfig(
            Component.text(key().toString()),
            MaterialIconSettings(Material.WRITABLE_BOOK)
        )
    }

    fun from(itemStack: ItemStack?): T?

    override fun compareTo(other: StackIdentifierParser<*>): Int {
        return other.priority().compareTo(this.priority())
    }

    interface DisplayConfiguration {
        val name: Component

        val icon: IconSettings?

        interface IconSettings

        @JvmRecord
        data class StackIconSettings(val stack: ItemStack) : IconSettings

        @JvmRecord
        data class MaterialIconSettings(val material: Material) : IconSettings

        @JvmRecord
        data class SimpleDisplayConfig(override val name: Component, override val icon: IconSettings) : DisplayConfiguration
    }
}
