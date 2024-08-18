package com.wolfyscript.scaffolding.spigot.api.wrappers

import com.wolfyscript.scaffolding.spigot.api.wrappers.world.BukkitLocation
import com.wolfyscript.scaffolding.spigot.api.wrappers.world.BukkitWorld
import com.wolfyscript.scaffolding.spigot.api.wrappers.world.entity.BukkitEntity
import com.wolfyscript.scaffolding.spigot.api.wrappers.world.entity.BukkitPlayer
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

fun World.wrap() : com.wolfyscript.scaffolding.wrappers.world.World = BukkitWorld(this)

fun Location.wrap() : com.wolfyscript.scaffolding.wrappers.world.Location = BukkitLocation(this)

fun Entity.wrap() : com.wolfyscript.scaffolding.wrappers.world.entity.Entity = BukkitEntity(this)

fun Player.wrap() : com.wolfyscript.scaffolding.wrappers.world.entity.Player = BukkitPlayer(this)

/* ******************
 * Unwrap on Spigot
 * ******************/

fun com.wolfyscript.scaffolding.wrappers.world.World.unwrap() : World = (this as BukkitWorld).bukkitRef

fun com.wolfyscript.scaffolding.wrappers.world.Location.unwrap() : Location = (this as BukkitLocation).bukkitRef

fun com.wolfyscript.scaffolding.wrappers.world.entity.Entity.unwrap() : Entity = (this as BukkitEntity<*>).bukkitRef

fun com.wolfyscript.scaffolding.wrappers.world.entity.Player.unwrap() : Player = (this as BukkitPlayer).bukkitRef