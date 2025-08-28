package com.wolfyscript.scafall.wrappers.world.entity

import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import java.util.*

interface Entity {

    val uuid: UUID

    val pos: ScafallPrecisePos

}
