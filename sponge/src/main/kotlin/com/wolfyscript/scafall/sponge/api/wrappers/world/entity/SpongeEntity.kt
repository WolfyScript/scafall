package com.wolfyscript.scafall.sponge.api.wrappers.world.entity

import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePosCommon
import com.wolfyscript.scafall.sponge.api.wrappers.SpongeRefWrapper
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import com.wolfyscript.scafall.wrappers.world.entity.Entity
import net.minecraft.world.phys.Vec3
import java.util.UUID

open class SpongeEntity<T: org.spongepowered.api.entity.Entity>(ref: T) : SpongeRefWrapper<T>(ref), Entity{

    override val uuid: UUID = ref.uniqueId()
    override val pos: ScafallPrecisePos
        get() {
            return ref.get()?.let {
                ScafallPrecisePosCommon(Vec3(it.position().x(), it.position().y(), it.position().z()))
            } ?: throw IllegalStateException("Player is not available!")
        }

}