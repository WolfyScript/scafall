package com.wolfyscript.scafall.paper

import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.paper.api.PaperAdventureUtil
import com.wolfyscript.scafall.spigotlike.server.ScafallSpigotLikeServer
import org.bukkit.Server

class ScafallServerPaper(val scafall: Scafall, bukkitServer: Server) : ScafallSpigotLikeServer(bukkitServer) {

    override val adventure: PaperAdventureUtil = PaperAdventureUtil(bukkitServer)

    override fun onLoad() {

    }

    override fun onUnload() {

    }

}