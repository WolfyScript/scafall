package com.wolfyscript.scafall.fabric.server

import com.wolfyscript.scafall.server.ScafallServer
import net.minecraft.server.MinecraftServer

class FabricScafallServer(override val minecraftServer: MinecraftServer) : ScafallServer {

    override val isDedicated: Boolean = minecraftServer.isDedicatedServer

    override fun onLoad() {

    }

    override fun onUnload() {

    }
}