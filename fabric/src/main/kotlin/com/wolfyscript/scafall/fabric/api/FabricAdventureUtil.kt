package com.wolfyscript.scafall.fabric.api

import com.google.gson.JsonParseException
import com.mojang.serialization.JsonOps
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.adventure.AdventureUtil
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.ComponentSerialization
import java.util.*

class FabricAdventureUtil(private val scafall: Scafall) : AdventureUtil {

    private val minecraftAudiences = MinecraftServerAudiences.of(scafall.server.minecraftServer)

    override fun player(uuid: UUID): Audience {
        return minecraftAudiences.player(uuid)
    }

    override fun all(): Audience {
        return minecraftAudiences.all()
    }

    override fun system(): Audience {
        return minecraftAudiences.console()
    }

    override fun toVanilla(component: Component): net.minecraft.network.chat.Component {
        val json = GsonComponentSerializer.gson().serializeToTree(component)
        val holder = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY)

        val result = ComponentSerialization.CODEC.decode(holder.createSerializationContext(JsonOps.INSTANCE), json)
        if (result.isError) {
            throw JsonParseException(result.error().get().message())
        }
        return result.result().get().first
    }
}