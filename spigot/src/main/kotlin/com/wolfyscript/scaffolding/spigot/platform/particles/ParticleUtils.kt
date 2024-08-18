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
package com.wolfyscript.scaffolding.spigot.platform.particles

import com.wolfyscript.scaffolding.ScaffoldingProvider
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.spigot.platform.particleAnimations
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import java.util.*

object ParticleUtils {
    private val activeAnimations: MutableMap<UUID, ParticleAnimation.Scheduler> = LinkedHashMap()

    fun spawnAnimationOnBlock(nameSpacedKey: Key, block: Block) {
        ScaffoldingProvider.get().registries.particleAnimations[nameSpacedKey]?.spawn(block)
    }

    fun spawnAnimationOnLocation(nameSpacedKey: Key, location: Location) {
        ScaffoldingProvider.get().registries.particleAnimations[nameSpacedKey]?.spawn(location)
    }

    fun spawnAnimationOnEntity(nameSpacedKey: Key, entity: Entity) {
        ScaffoldingProvider.get().registries.particleAnimations[nameSpacedKey]?.spawn(entity)
    }

    fun spawnAnimationOnPlayer(nameSpacedKey: Key, player: Player, equipmentSlot: EquipmentSlot) {
        ScaffoldingProvider.get().registries.particleAnimations[nameSpacedKey]?.spawn(player, equipmentSlot)
    }

    /**
     * Stops the Effect that is currently active.
     * If the uuid is null or the list doesn't contain it this method does nothing.
     *
     * @param uuid The [UUID] of the animation.
     */
    @JvmStatic
    fun stopAnimation(uuid: UUID?) {
        if (uuid != null) {
            val scheduler = activeAnimations[uuid]
            if (scheduler != null) {
                scheduler.stop()
                activeAnimations.remove(uuid)
            }
        }
    }

    @JvmStatic
    fun removeScheduler(uuid: UUID?) {
        if (uuid != null) {
            val scheduler = activeAnimations[uuid]
            if (scheduler != null) {
                activeAnimations.remove(uuid)
            }
        }
    }

    @JvmStatic
    fun addScheduler(scheduler: ParticleAnimation.Scheduler): UUID {
        var id = UUID.randomUUID()
        while (activeAnimations.containsKey(id)) {
            id = UUID.randomUUID()
        }
        activeAnimations[id] = scheduler
        return id
    }

    /**
     * @return A set containing the [UUID]s of the active animations. This Set is unmodifiable!
     */
    fun getActiveAnimations(): Set<UUID> {
        return java.util.Set.copyOf(activeAnimations.keys)
    }
}
