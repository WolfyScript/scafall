package com.wolfyscript.scafall.server

import com.wolfyscript.scafall.adventure.AdventureUtil
import com.wolfyscript.scafall.loader.module.Server
import net.minecraft.server.MinecraftServer

/**
 * The part of the scafall API that is only available on a server.
 * It provides access to features specific to the server (dedicated or integrated)
 *
 * On a dedicated server, there will be only one instance of this.
 *
 * On the client-side a new instance of this [ScafallServer] will be created for each Minecraft server and
 * disposed when that server is shutdown.
 */
interface ScafallServer : Server {

    /**
     * Either the [net.minecraft.server.dedicated.DedicatedServer] or [net.minecraft.client.server.IntegratedServer]
     */
    val minecraftServer: MinecraftServer

    val isDedicated: Boolean

    val adventure: AdventureUtil

}