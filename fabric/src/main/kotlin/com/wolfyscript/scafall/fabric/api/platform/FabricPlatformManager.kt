package com.wolfyscript.scafall.fabric.api.platform

import com.wolfyscript.scafall.platform.CommonPlatformManager
import com.wolfyscript.scafall.fabric.api.ScafallFabric
import com.wolfyscript.scafall.platform.PlatformType

class FabricPlatformManager(scafall: ScafallFabric) : CommonPlatformManager(scafall.classLoader) {

    override val platformType: PlatformType = PlatformType.FABRIC

}