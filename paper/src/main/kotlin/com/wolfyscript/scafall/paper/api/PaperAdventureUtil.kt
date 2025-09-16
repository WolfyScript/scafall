package com.wolfyscript.scafall.paper.api

import com.google.gson.JsonParseException
import com.mojang.serialization.JsonOps
import com.wolfyscript.scafall.adventure.AdventureUtil
import com.wolfyscript.scafall.paper.ScafallPaper
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import java.util.UUID

class PaperAdventureUtil(val scafall: ScafallPaper) : AdventureUtil {

    override fun player(uuid: UUID): Audience {
        return scafall.plugin.server.getPlayer(uuid) ?: Audience.empty()
    }

    override fun all(): Audience {
        return scafall.plugin.server
    }

    override fun system(): Audience {
        return scafall.plugin.server.consoleSender
    }

    override fun toVanilla(component: net.kyori.adventure.text.Component): Component {
        val json = GsonComponentSerializer.gson().serializeToTree(component)
        val holder = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY)

        val result = net.minecraft.network.chat.ComponentSerialization.CODEC.decode(holder.createSerializationContext(JsonOps.INSTANCE), json)
        if (result.isError) {
            throw JsonParseException(result.error().get().message())
        }
        return result.result().get().first
    }
}