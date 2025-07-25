package com.wolfyscript.scafall.fabric.api.platform

import com.wolfyscript.scafall.common.api.platform.CommonPlatformManager
import com.wolfyscript.scafall.fabric.api.ScafallFabricServer
import com.wolfyscript.scafall.platform.PlatformType

class FabricPlatformManager(scafall: ScafallFabricServer) : CommonPlatformManager(scafall.classLoader) {

    override val platformType: PlatformType = PlatformType.FABRIC

}