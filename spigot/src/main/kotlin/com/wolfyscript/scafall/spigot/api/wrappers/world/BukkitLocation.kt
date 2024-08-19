package com.wolfyscript.scafall.spigot.api.wrappers.world

import com.wolfyscript.scafall.spigot.api.wrappers.BukkitRefAdapter
import org.bukkit.Location

class BukkitLocation(location: Location) : BukkitRefAdapter<Location>(location), com.wolfyscript.scafall.wrappers.world.Location