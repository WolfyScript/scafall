package com.wolfyscript.scafall.spigot.compat.mythicmobs

import com.wolfyscript.scafall.compat.DependencyResolverSettings
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigot.api.wrappers.utils.wrap
import com.wolfyscript.scafall.spigot.compat.PluginDependencyResolver
import com.wolfyscript.scafall.spigot.compat.PluginDependencyResolverSettings
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import io.lumine.mythic.bukkit.MythicBukkit

@DependencyResolverSettings(PluginDependencyResolver::class)
@PluginDependencyResolverSettings(MythicMobsDependency::class)
class MythicMobsStackIdentifier(val mythicType: String) : ItemStackIdentifier {

    override fun matches(
        stack: ItemStackLike,
        matchTags: Boolean,
    ): Boolean {
        if (stack.isEmpty) return false
        val type = MythicBukkit.inst().itemManager.getMythicTypeFromItem(stack.unwrapSpigot()) ?: return false
        return type == mythicType
    }

    override fun create(): ItemStack {
        return MythicBukkit.inst().itemManager.getItemStack(mythicType).wrap()
    }
}