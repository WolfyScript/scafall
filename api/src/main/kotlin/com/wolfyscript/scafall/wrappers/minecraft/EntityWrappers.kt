package com.wolfyscript.scafall.wrappers.minecraft

import com.wolfyscript.scafall.wrappers.ScafallBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity

fun BlockEntity.wrap(): ScafallBlockEntity = ScafallBlockEntityImpl(this)

fun ScafallBlockEntity.unwrap(): BlockEntity = (this as ScafallBlockEntityImpl).entity

internal class ScafallBlockEntityImpl(val entity: BlockEntity) : ScafallBlockEntity
