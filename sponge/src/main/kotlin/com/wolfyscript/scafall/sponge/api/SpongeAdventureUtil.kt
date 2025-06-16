package com.wolfyscript.scafall.sponge.api

import com.wolfyscript.scafall.adventure.AdventureUtil
import com.wolfyscript.scafall.Scafall
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.MutableComponent
import org.spongepowered.api.Sponge
import org.spongepowered.api.adventure.Audiences
import java.util.*

class SpongeAdventureUtil(private val scafall: Scafall) : AdventureUtil {

    override fun player(uuid: UUID): Audience {
        return Sponge.server().player(uuid).map { it as Audience }.orElseGet { Audience.empty() }
    }

    override fun all(): Audience {
        return Audiences.server()
    }

    override fun system(): Audience {
        return Audiences.system()
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