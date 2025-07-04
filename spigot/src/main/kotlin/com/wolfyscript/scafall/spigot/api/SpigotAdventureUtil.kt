package com.wolfyscript.scafall.spigot.api

import com.google.gson.JsonParseException
import com.mojang.serialization.JsonOps
import com.wolfyscript.scafall.adventure.AdventureUtil
import com.wolfyscript.scafall.Scafall
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
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

    override fun toVanilla(component: Component): net.minecraft.network.chat.Component {
        val json = GsonComponentSerializer.gson().serializeToTree(component)
        val holder = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY)

        val result = net.minecraft.network.chat.ComponentSerialization.CODEC.decode(holder.createSerializationContext(JsonOps.INSTANCE), json)
        if (result.isError) {
            throw JsonParseException(result.error().get().message())
        }
        return result.result().get().first
    }
}