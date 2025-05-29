package com.wolfyscript.scafall.server

import net.minecraft.server.MinecraftServer

interface ScafallServer {

    /**
     * Either the [net.minecraft.server.dedicated.DedicatedServer] or [net.minecraft.client.server.IntegratedServer]
     */
    val minecraftServer: MinecraftServer

    val isDedicated: Boolean

}