package com.wolfyscript.scaffolding.spigot.api.wrappers.world.entity

import com.wolfyscript.scaffolding.deserialize
import com.wolfyscript.scaffolding.text
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class BukkitPlayer(player: Player) : BukkitEntity<Player>(player), com.wolfyscript.scaffolding.wrappers.world.entity.Player {

    override var displayName: Component?
        get() = bukkitRef.displayName.deserialize()
        set(value) {}

}