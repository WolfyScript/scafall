package com.wolfyscript.scafall.spigotlike.compat.mythicmobs

import com.wolfyscript.scafall.compat.DependencyResolverSettings
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigot.api.wrappers.utils.wrap
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyResolver
import com.wolfyscript.scafall.spigotlike.compat.PluginDependencyResolverSettings
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack
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

    override fun create(): ScafallItemStack {
        return MythicBukkit.inst().itemManager.getItemStack(mythicType).wrap()
    }
}