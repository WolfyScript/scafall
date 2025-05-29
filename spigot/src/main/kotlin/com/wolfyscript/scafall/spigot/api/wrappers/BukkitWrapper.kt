package com.wolfyscript.scafall.spigot.api.wrappers

import com.wolfyscript.scafall.spigot.api.wrappers.world.BukkitWorld
import com.wolfyscript.scafall.spigot.api.wrappers.world.entity.BukkitEntity
import org.bukkit.Color
import org.bukkit.World
import org.bukkit.entity.Entity

fun World.wrap(): com.wolfyscript.scafall.wrappers.world.World = BukkitWorld(this)

fun Color.wrap(): com.wolfyscript.scafall.wrappers.world.Color =
    com.wolfyscript.scafall.wrappers.world.Color(this.red, this.green, this.blue, this.alpha)

/* ******************
 * Unwrap on Spigot
 * ******************/

fun com.wolfyscript.scafall.wrappers.world.World.unwrap(): World = (this as BukkitWorld).bukkitRef

fun com.wolfyscript.scafall.wrappers.world.entity.Entity.unwrap(): Entity = (this as BukkitEntity<*>).bukkitRef

fun com.wolfyscript.scafall.wrappers.world.Color.unwrap(): Color =
    Color.fromARGB(this.alpha.toInt(), this.red.toInt(), this.green.toInt(), this.blue.toInt())