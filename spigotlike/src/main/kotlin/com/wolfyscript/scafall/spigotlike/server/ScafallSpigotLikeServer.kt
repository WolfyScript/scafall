package com.wolfyscript.scafall.spigotlike.server

import com.wolfyscript.scafall.server.ScafallServer
import net.minecraft.server.MinecraftServer
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.CraftServer

class ScafallSpigotLikeServer : ScafallServer {

    override val minecraftServer: MinecraftServer = (Bukkit.getServer() as CraftServer).server

    override val isDedicated: Boolean = minecraftServer.isDedicatedServer

}