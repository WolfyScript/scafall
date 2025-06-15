package com.wolfyscript.scafall.wrappers.world.entity

import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import com.wolfyscript.scafall.wrappers.world.Vector3D
import com.wolfyscript.scafall.wrappers.world.World
import org.jetbrains.annotations.Contract
import java.util.*

interface Entity {

    val uuid: UUID

    val pos: ScafallPrecisePos

}
