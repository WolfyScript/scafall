package com.wolfyscript.scafall.spigot.compat.magic

import com.elmakers.mine.bukkit.api.magic.MagicAPI
import com.fasterxml.jackson.annotation.JsonIgnore
import com.wolfyscript.scafall.compat.DependencyResolverSettings
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigot.api.wrappers.utils.wrap
import com.wolfyscript.scafall.spigot.compat.PluginDependencyResolver
import com.wolfyscript.scafall.spigot.compat.PluginDependencyResolverSettings
import com.wolfyscript.scafall.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import org.bukkit.Bukkit

@DependencyResolverSettings(PluginDependencyResolver::class)
@PluginDependencyResolverSettings(MagicDependency::class)
class MagicStackIdentifier(val itemKey: String) : ItemStackIdentifier {

    @JsonIgnore
    val magicAPI = Bukkit.getPluginManager().getPlugin("Magic") as? MagicAPI ?: error("Could not find Magic API!")

    override fun matches(
        stack: ItemStackLike<*, *>,
        matchTags: Boolean,
    ): Boolean {
        return magicAPI.controller.getItemKey(stack.unwrapSpigot()) == itemKey
    }

    override fun create(): ItemStack {
        return magicAPI.controller.createItem(itemKey)?.wrap() ?: return net.minecraft.world.item.ItemStack.EMPTY.wrap()
    }
}