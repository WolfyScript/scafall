package com.wolfyscript.scaffolding.spigot.api.wrappers.world.entity

import com.wolfyscript.scaffolding.spigot.api.wrappers.BukkitRefAdapter
import com.wolfyscript.scaffolding.spigot.api.wrappers.wrap
import com.wolfyscript.scaffolding.wrappers.world.Location
import com.wolfyscript.scaffolding.wrappers.world.Vector3D
import com.wolfyscript.scaffolding.wrappers.world.World
import com.wolfyscript.scaffolding.wrappers.world.entity.Entity
import java.util.*

open class BukkitEntity<T: org.bukkit.entity.Entity>(entity: T) : BukkitRefAdapter<T>(entity), Entity {

    override fun uuid(): UUID = bukkitRef.uniqueId

    override val location: Location
        get() = bukkitRef.location.wrap()

    override fun getLocation(loc: Location): Location? {
        TODO()
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