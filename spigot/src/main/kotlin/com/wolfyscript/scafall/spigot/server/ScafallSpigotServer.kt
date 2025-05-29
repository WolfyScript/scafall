package com.wolfyscript.scafall.spigot.server

import com.wolfyscript.scafall.server.ScafallServer
import net.minecraft.server.MinecraftServer
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.CraftServer

class ScafallSpigotServer : ScafallServer {

    override val minecraftServer: MinecraftServer = (Bukkit.getServer() as CraftServer).server

    override val isDedicated: Boolean = minecraftServer.isDedicatedServer

}