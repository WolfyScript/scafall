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
package com.wolfyscript.scaffolding.spigot.platform.world.items

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.wolfyscript.scaffolding.Scaffolding
import com.wolfyscript.scaffolding.ScaffoldingProvider
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.spigot.platform.particleAnimations
import com.wolfyscript.scaffolding.spigot.platform.particles.ParticleAnimation
import com.wolfyscript.scaffolding.spigot.platform.particles.ParticleLocation
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import java.util.*

/**
 * This class contains settings of particle animations for different locations they can be spawned at. <br></br>
 * It is used for the CustomItem, and spawns/stops the animations according to their active location.<br></br>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
class ParticleContent {
    var location: Settings? = null
    var block: Settings? = null
    var entity: Settings? = null
    var player: PlayerSettings? = null

    /**
     * Gets the animation of the [PlayerSettings] of the specified [EquipmentSlot].<br></br>
     * If the equipment slot is null, it will get the parent animation of the [PlayerSettings].
     *
     * @param equipmentSlot The equipment slot to get the animation for, or null for the parent animation.
     * @return The animation for the players' equipment slot, or the parent animation if equipment slot is null.
     */
    fun getPlayerAnimation(equipmentSlot: EquipmentSlot?): ParticleAnimation? {
        if (equipmentSlot == null) {
            return getAnimation(ParticleLocation.PLAYER)
        }
        return if (player != null) player!!.getByEquipmentSlot(equipmentSlot) else null
    }

    /**
     * Gets the animation set for the specified [ParticleLocation].<br></br>
     *
     * <br></br>**Note:**<br></br>
     * If using the [ParticleLocation.PLAYER] it will always return the parent animation of the [PlayerSettings]!<br></br>
     * Use [.getPlayerAnimation] instead if you want to get equipment slot specific animations!
     *
     * @param location The location to get the animation for.
     * @return The animation set for the specified [ParticleLocation]
     */
    fun getAnimation(location: ParticleLocation): ParticleAnimation? {
        val setting = when (location) {
            ParticleLocation.BLOCK -> block
            ParticleLocation.PLAYER -> player
            ParticleLocation.ENTITY -> entity
            ParticleLocation.LOCATION -> this.location
            else -> null
        }
        return setting?.getAnimation()
    }

    /**
     * Sets the animation for the specified [ParticleLocation].<br></br>
     *
     * <br></br><**Note:**<br></br>
     * When using [ParticleLocation.PLAYER] it will only set the parent animation.<br></br>
     * To edit equipment specific animations use [.setPlayer].
     *
     * @param location The [ParticleLocation] to set the animation for.
     * @param animation The new animation to set.
     */
    fun setAnimation(location: ParticleLocation, animation: ParticleAnimation) {
        when (location) {
            ParticleLocation.BLOCK -> block =
                Settings(animation)

            ParticleLocation.PLAYER -> player =
                PlayerSettings(animation)

            ParticleLocation.ENTITY -> entity =
                Settings(animation)

            ParticleLocation.LOCATION -> this.location =
                Settings(animation)

            else -> {}
        }
    }

    /**
     * Spawns the animation for the specified equipment slot if it is available.
     *
     * @param player The player to spawn the animation on.
     * @param slot The [EquipmentSlot] of the animation.
     */
    fun spawn(player: Player, slot: EquipmentSlot?) {
        val animation = getPlayerAnimation(slot)
        animation?.spawn(player, slot)
    }

    override fun toString(): String {
        return "ParticleContent{" +
                "location=" + location +
                ", block=" + block +
                ", entity=" + entity +
                ", player=" + player +
                '}'
    }

    open class Settings {
        @JsonIgnore
        private var wolfyUtils: Scaffolding? = null
        private var animation: ParticleAnimation? = null

        protected constructor(wolfyUtils: Scaffolding?) {
            this.wolfyUtils = wolfyUtils
        }

        constructor(key: Key) {
            setAnimation(key)
        }

        constructor(animation: ParticleAnimation) {
            setAnimation(animation)
        }

        @JsonGetter
        fun getAnimation(): ParticleAnimation? {
            return animation
        }

        @JsonSetter(nulls = Nulls.SKIP)
        fun setAnimation(animation: ParticleAnimation) {
            this.animation = Objects.requireNonNull(animation, "Animation cannot be null!")
        }

        fun setAnimation(animation: Key) {
            this.animation = ScaffoldingProvider.get().registries.particleAnimations[animation]
        }

        /**
         * Used to convert old config "effect" setting to the new version.
         */
        @Deprecated("")
        @JsonSetter("effect")
        private fun setEffect(animation: ParticleAnimation?) {
            if (animation != null) {
                setAnimation(animation)
            }
        }

        override fun toString(): String {
            return "Settings{" +
                    "animation=" + animation +
                    '}'
        }
    }

    /**
     * Contains player specific animations per EquipmentSlot.<br></br>
     * The animation from the parent value ([.getAnimation], [.setAnimation], and [.setAnimation]) is used if no equipment slot is selected (see [.getPlayerAnimation]).
     */
    class PlayerSettings : Settings {
        var head: ParticleAnimation? = null
        var chest: ParticleAnimation? = null
        var legs: ParticleAnimation? = null
        var feet: ParticleAnimation? = null
        var mainHand: ParticleAnimation? = null
        var offHand: ParticleAnimation? = null

        constructor(wolfyUtils: Scaffolding?) : super(wolfyUtils)

        constructor(key: Key) : super(key)

        constructor(animation: ParticleAnimation) : super(animation)

        fun getByEquipmentSlot(equipmentSlot: EquipmentSlot): ParticleAnimation {
            return when (equipmentSlot) {
                EquipmentSlot.HEAD -> head!!
                EquipmentSlot.CHEST -> chest!!
                EquipmentSlot.LEGS -> legs!!
                EquipmentSlot.FEET -> feet!!
                EquipmentSlot.HAND -> mainHand!!
                EquipmentSlot.OFF_HAND -> offHand!!
            }
        }

        override fun toString(): String {
            return "PlayerSettings{" +
                    "animation=" + getAnimation() +
                    ", head=" + head +
                    ", chest=" + chest +
                    ", legs=" + legs +
                    ", feet=" + feet +
                    ", mainHand=" + mainHand +
                    ", offHand=" + offHand +
                    '}'
        }
    }
}
