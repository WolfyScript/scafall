package com.wolfyscript.scafall.spigot.api.wrappers

import com.wolfyscript.scafall.spigot.api.wrappers.world.BukkitLocation
import com.wolfyscript.scafall.spigot.api.wrappers.world.BukkitWorld
import com.wolfyscript.scafall.spigot.api.wrappers.world.entity.BukkitEntity
import com.wolfyscript.scafall.spigot.api.wrappers.world.entity.BukkitPlayer
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

fun World.wrap() : com.wolfyscript.scafall.wrappers.world.World = BukkitWorld(this)

fun Location.wrap() : com.wolfyscript.scafall.wrappers.world.Location = BukkitLocation(this)

fun Entity.wrap() : com.wolfyscript.scafall.wrappers.world.entity.Entity = BukkitEntity(this)

fun Player.wrap() : com.wolfyscript.scafall.wrappers.world.entity.Player = BukkitPlayer(this)

/* ******************
 * Unwrap on Spigot
 * ******************/

fun com.wolfyscript.scafall.wrappers.world.World.unwrap() : World = (this as BukkitWorld).bukkitRef

fun com.wolfyscript.scafall.wrappers.world.Location.unwrap() : Location = (this as BukkitLocation).bukkitRef

fun com.wolfyscript.scafall.wrappers.world.entity.Entity.unwrap() : Entity = (this as BukkitEntity<*>).bukkitRef

fun com.wolfyscript.scafall.wrappers.world.entity.Player.unwrap() : Player = (this as BukkitPlayer).bukkitRef