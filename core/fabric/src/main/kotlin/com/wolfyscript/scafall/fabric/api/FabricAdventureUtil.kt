package com.wolfyscript.scafall.fabric.api

import com.wolfyscript.scafall.adventure.AdventureUtil
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.platform.modcommon.MinecraftServerAudiences
import net.minecraft.server.MinecraftServer
import java.util.*

class FabricAdventureUtil(minecraftServer: MinecraftServer) : AdventureUtil {

    private val minecraftAudiences = MinecraftServerAudiences.of(minecraftServer)

    override fun player(uuid: UUID): Audience {
        return minecraftAudiences.player(uuid)
    }

    override fun all(): Audience {
        return minecraftAudiences.all()
    }

    override fun system(): Audience {
        return minecraftAudiences.console()
    }

}