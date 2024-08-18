/*
 *       WolfyUtilities, APIs and Utilities for Minecraft Spigot plugins
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.wolfyscript.scaffolding.spigot.platform.world.items.actions

import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.scaffoldingKey
import org.bukkit.Sound
import org.bukkit.SoundCategory

class ActionSound protected constructor() :
    Action<DataLocation>(
        KEY,
        DataLocation::class.java
    ) {
    private var sound: Sound? = null
    private var volume = 1.0f
    private var pitch = 1.0f
    private var category = SoundCategory.PLAYERS
    private var onlyForPlayer = false

    override fun execute(core: Scaffolding, data: DataLocation) {
        val location = data.location
        if (location.world == null) {
            return
        }
        if (data is DataPlayer && onlyForPlayer) {
            sound?.let { data.player.playSound(location, it, category, volume, pitch) }
        } else {
            location.world.playSound(location, sound!!, category, volume, pitch)
        }
    }

    fun setSound(sound: Sound) {
        this.sound = sound
    }

    fun setCategory(category: SoundCategory) {
        this.category = category
    }

    fun setOnlyForPlayer(onlyForPlayer: Boolean) {
        this.onlyForPlayer = onlyForPlayer
    }

    fun setPitch(pitch: Float) {
        this.pitch = pitch
    }

    fun setVolume(volume: Float) {
        this.volume = volume
    }

    companion object {
        val KEY: Key = scaffoldingKey("location/sound")
    }
}
