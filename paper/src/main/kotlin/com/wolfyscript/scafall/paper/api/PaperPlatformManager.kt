package com.wolfyscript.scafall.paper.api

import com.wolfyscript.scafall.common.api.platform.CommonPlatformManager
import com.wolfyscript.scafall.paper.ScafallPaper
import com.wolfyscript.scafall.platform.PlatformType
import io.papermc.paper.ServerBuildInfo

class PaperPlatformManager(scafallPaper: ScafallPaper) : CommonPlatformManager(scafallPaper.classLoader) {

    override val platformType: PlatformType = detectPlatform()

    private fun detectPlatform(): PlatformType {
        // We can use the API (which is still experimental though) to check which platform it is
        if (ServerBuildInfo.buildInfo().isBrandCompatible(net.kyori.adventure.key.Key.key("papermc", "folia"))) {
            return PlatformType.FOLIA
        }
        if (ServerBuildInfo.buildInfo().isBrandCompatible(net.kyori.adventure.key.Key.key("purpurmc", "purpur"))) {
            return PlatformType.PURPUR
        }
        return PlatformType.PAPER
    }
}