package com.wolfyscript.scafall.spigot

import com.wolfyscript.scafall.spigot.api.SpigotAdventureUtil
import com.wolfyscript.scafall.spigotlike.server.ScafallSpigotLikeServer
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

class ScafallServerSpigot(private val bukkitPlugin: JavaPlugin, bukkitServer: Server) : ScafallSpigotLikeServer(bukkitServer) {

    override val adventure: SpigotAdventureUtil = SpigotAdventureUtil()

    override fun onLoad() {

        adventure.init(bukkitPlugin)
    }

    override fun onUnload() {

        adventure.unload()
    }


}