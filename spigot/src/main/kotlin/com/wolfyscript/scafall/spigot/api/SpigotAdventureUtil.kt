package com.wolfyscript.scafall.spigot.api

import com.wolfyscript.scafall.AdventureUtil
import com.wolfyscript.scafall.Scafall
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import java.util.*

class SpigotAdventureUtil(private val scafall: Scafall) : AdventureUtil {

    private var backingAdventure: BukkitAudiences? = null
    private val adventure : BukkitAudiences
        get() {
            checkNotNull(backingAdventure) { "Tried to access Adventure when the plugin was disabled!" }
            return backingAdventure!!
        }

    fun init() {
        this.backingAdventure = BukkitAudiences.create(scafall.corePlugin.into().plugin)
    }

    fun unload() {
        if (backingAdventure != null) {
            backingAdventure!!.close()
            backingAdventure = null
        }
    }

    override fun player(uuid: UUID): Audience {
        return adventure.player(uuid)
    }

    override fun all(): Audience {
        return adventure.all()
    }

    override fun system(): Audience {
        return adventure.console()
    }
}