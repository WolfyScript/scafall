package com.wolfyscript.scafall.spigot.api.wrappers.world.entity

import com.wolfyscript.scafall.spigot.api.wrappers.BukkitRefAdapter
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import com.wolfyscript.scafall.wrappers.world.entity.Entity
import java.util.*

open class BukkitEntity<T: org.bukkit.entity.Entity>(entity: T,
                                                     override val uuid: UUID,
                                                     override val pos: ScafallPrecisePos
) : BukkitRefAdapter<T>(entity), Entity {



}