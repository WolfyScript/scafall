package com.wolfyscript.scafall.common.api.wrappers

import com.wolfyscript.scafall.wrappers.ScafallBlockEntity
import com.wolfyscript.scafall.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.ScafallBlockPos
import net.minecraft.world.level.block.entity.BlockEntity

class ScafallBlockEntityCommon(val entity: BlockEntity) : ScafallBlockEntity {

    val blockPos: ScafallBlockPos = entity.blockPos.wrap()

}