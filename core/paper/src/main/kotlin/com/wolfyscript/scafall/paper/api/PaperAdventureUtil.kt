package com.wolfyscript.scafall.paper.api

import com.wolfyscript.scafall.adventure.AdventureUtil
import net.kyori.adventure.audience.Audience
import org.bukkit.Server
import java.util.UUID

class PaperAdventureUtil(val bukkitServer: Server) : AdventureUtil {

    override fun player(uuid: UUID): Audience {
        return bukkitServer.getPlayer(uuid) ?: Audience.empty()
    }

    override fun all(): Audience {
        return bukkitServer
    }

    override fun system(): Audience {
        return bukkitServer.consoleSender
    }

}