package com.wolfyscript.scafall.spigot

import com.wolfyscript.scafall.spigotlike.server.ScafallSpigotLikeServer
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

class ScafallServerSpigot(private val bukkitPlugin: JavaPlugin, bukkitServer: Server) : ScafallSpigotLikeServer(bukkitServer) {

    override fun onLoad() {
    }

    override fun onUnload() {
    }


}