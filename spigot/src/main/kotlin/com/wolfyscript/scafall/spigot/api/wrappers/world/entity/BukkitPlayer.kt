package com.wolfyscript.scafall.spigot.api.wrappers.world.entity

import com.wolfyscript.scafall.deserialize
import com.wolfyscript.scafall.text
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

class BukkitPlayer(player: Player) : BukkitEntity<Player>(player), com.wolfyscript.scafall.wrappers.world.entity.Player {

    override var displayName: Component?
        get() = bukkitRef.displayName.deserialize()
        set(value) {}

}