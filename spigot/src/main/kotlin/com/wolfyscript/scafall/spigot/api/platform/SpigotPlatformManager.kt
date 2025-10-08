package com.wolfyscript.scafall.spigot.api.platform

import com.wolfyscript.scafall.common.api.platform.CommonPlatformManager
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.spigot.ScafallSpigot

class SpigotPlatformManager internal constructor(scafallSpigot: ScafallSpigot) : CommonPlatformManager(scafallSpigot.classLoader) {

    override val platformType: PlatformType = PlatformType.SPIGOT

}