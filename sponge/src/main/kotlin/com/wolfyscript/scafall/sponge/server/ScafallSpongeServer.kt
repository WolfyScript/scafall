package com.wolfyscript.scafall.sponge.server

import com.wolfyscript.scafall.server.ScafallServer
import net.minecraft.server.MinecraftServer
import org.spongepowered.common.SpongeCommon

class ScafallSpongeServer : ScafallServer {

    override val minecraftServer: MinecraftServer = SpongeCommon.server() // TODO: naive implementation for now. Server may not be available
    override val isDedicated: Boolean = minecraftServer.isDedicatedServer

}