package com.wolfyscript.scafall.sponge.api.platform

import com.wolfyscript.scafall.platform.CommonPlatformManager
import com.wolfyscript.scafall.platform.PlatformType
import com.wolfyscript.scafall.sponge.api.ScafallSponge

class PlatformManagerImpl(scafallSponge: ScafallSponge) : CommonPlatformManager(scafallSponge.bootstrap.classLoader) {

    override val platformType: PlatformType = PlatformType.SPONGE

}