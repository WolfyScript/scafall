package com.wolfyscript.scafall.wrappers.minecraft

import com.wolfyscript.scafall.wrappers.ScafallBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * Wraps this [BlockEntity] in a [ScafallBlockEntity].
 *
 * @return A new [ScafallBlockEntity] instance that wraps this block entity.
 */
fun BlockEntity.wrap(): ScafallBlockEntity = ScafallBlockEntityImpl(this)

/**
 * Unwraps the [ScafallBlockEntity] to its underlying [BlockEntity].
 *
 * @return The unwrapped [BlockEntity] instance.
 */
fun ScafallBlockEntity.unwrap(): BlockEntity = (this as ScafallBlockEntityImpl).entity

internal class ScafallBlockEntityImpl(val entity: BlockEntity) : ScafallBlockEntity
