package com.wolfyscript.scafall.spigotlike.server

import com.wolfyscript.scafall.server.ScafallServer
import net.minecraft.server.MinecraftServer
import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.craftbukkit.CraftServer

abstract class ScafallSpigotLikeServer(bukkitServer: Server) : ScafallServer {

    override val minecraftServer: MinecraftServer = (bukkitServer as CraftServer).server

    override val isDedicated: Boolean = minecraftServer.isDedicatedServer

}