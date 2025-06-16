package com.wolfyscript.scafall.spigot.api

import com.wolfyscript.scafall.adventure.AdventureUtil
import com.wolfyscript.scafall.Scafall
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.MutableComponent
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

    override fun toVanilla(component: Component): MutableComponent {
        val json = GsonComponentSerializer.gson().serialize(component)
        val holder = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY)
        val mutableComponent = net.minecraft.network.chat.Component.Serializer.fromJson(json, holder)
        if (mutableComponent == null) {
            throw IllegalStateException("Failed to serialize Component to vanilla! This should not happen for valid adventure components!")
        }
        return mutableComponent
    }
}