package com.wolfyscript.scafall.sponge.api.wrappers.world.entity

import com.wolfyscript.scafall.sponge.api.wrappers.SpongeRefWrapper
import com.wolfyscript.scafall.wrappers.world.Location
import com.wolfyscript.scafall.wrappers.world.Vector3D
import com.wolfyscript.scafall.wrappers.world.World
import com.wolfyscript.scafall.wrappers.world.entity.Entity
import java.util.UUID

open class SpongeEntity<T: org.spongepowered.api.entity.Entity>(ref: T) : SpongeRefWrapper<T>(ref), Entity{

    override fun uuid(): UUID {
        TODO("Not yet implemented")
    }

    override val location: Location
        get() = TODO("Not yet implemented")

    override fun getLocation(loc: Location): Location? {
        TODO("Not yet implemented")
    }

    override var velocity: Vector3D
        get() = TODO("Not yet implemented")
        set(value) {}
    override val height: Double
        get() = TODO("Not yet implemented")
    override val width: Double
        get() = TODO("Not yet implemented")
    override val isOnGround: Boolean
        get() = TODO("Not yet implemented")
    override val isInWater: Boolean
        get() = TODO("Not yet implemented")
    override val world: World
        get() = TODO("Not yet implemented")

    override fun setRotation(yaw: Float, pitch: Float) {
        TODO("Not yet implemented")
    }
}