package com.wolfyscript.scafall.fabric.server

import com.wolfyscript.scafall.server.ScafallServer
import net.fabricmc.fabric.mixin.message.MinecraftServerMixin
import net.fabricmc.loader.api.FabricLoader
import net.kyori.adventure.platform.modcommon.impl.client.mixin.minecraft.MinecraftMixin
import net.minecraft.server.MinecraftServer
import org.spongepowered.asm.mixin.FabricUtil

class FabricScafallServer(override val minecraftServer: MinecraftServer) : ScafallServer {

    override val isDedicated: Boolean = minecraftServer.isDedicatedServer
}