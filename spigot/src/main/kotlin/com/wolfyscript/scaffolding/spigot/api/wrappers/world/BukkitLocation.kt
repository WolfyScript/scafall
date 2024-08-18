package com.wolfyscript.scaffolding.spigot.api.wrappers.world

import com.wolfyscript.scaffolding.spigot.api.wrappers.BukkitRefAdapter
import org.bukkit.Location

class BukkitLocation(location: Location) : BukkitRefAdapter<Location>(location), com.wolfyscript.scaffolding.wrappers.world.Location