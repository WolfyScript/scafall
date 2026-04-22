package com.wolfyscript.scafall.server

import com.wolfyscript.scafall.adventure.AdventureUtil
import com.wolfyscript.scafall.loader.module.Server
import net.minecraft.server.MinecraftServer

interface ScafallServer : Server {

    /**
     * Either the [net.minecraft.server.dedicated.DedicatedServer] or [net.minecraft.client.server.IntegratedServer]
     */
    val minecraftServer: MinecraftServer

    val isDedicated: Boolean

    val adventure: AdventureUtil

}