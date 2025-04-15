package com.wolfyscript.scafall.spigot.api.platform

import com.wolfyscript.scafall.common.api.platform.CommonPlatformManager
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.spigot.api.ScafallSpigot
import io.papermc.paper.ServerBuildInfo

class SpigotPlatformManager internal constructor(scafallSpigot: ScafallSpigot) : CommonPlatformManager(scafallSpigot.bootstrap.classLoader) {

    override val platformType: PlatformType = detectPlatform()

    private fun detectPlatform(): PlatformType {
        val isPaper: Boolean = try {
            Class.forName("io.papermc.paper.ServerBuildInfo")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
        if (isPaper) {
            // We can use the API (which is still experimental though) to check which platform it is
            if (ServerBuildInfo.buildInfo().isBrandCompatible(net.kyori.adventure.key.Key.key("papermc", "folia"))) {
                return PlatformType.FOLIA
            }
            if (ServerBuildInfo.buildInfo().isBrandCompatible(net.kyori.adventure.key.Key.key("purpurmc", "purpur"))) {
                return PlatformType.PURPUR
            }
            return PlatformType.PAPER
        }
        return PlatformType.SPIGOT
    }
}