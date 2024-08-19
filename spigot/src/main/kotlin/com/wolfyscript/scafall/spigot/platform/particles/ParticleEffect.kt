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
package com.wolfyscript.scafall.spigot.platform.particles

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.JsonNode
import com.google.common.base.Preconditions
import com.wolfyscript.scafall.ScafallProvider.Companion.get
import com.wolfyscript.scafall.config.jackson.JacksonUtil.objectMapper
import com.wolfyscript.scafall.config.jackson.OptionalKeyReference
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Keyed
import com.wolfyscript.scafall.spigot.platform.particles.animators.Animator
import com.wolfyscript.scafall.spigot.platform.particles.animators.AnimatorBasic
import com.wolfyscript.scafall.spigot.platform.particles.pos.ParticlePos
import com.wolfyscript.scafall.spigot.platform.particles.pos.ParticlePosLocation
import com.wolfyscript.scafall.spigot.platform.particles.timer.Timer
import com.wolfyscript.scafall.spigot.platform.particles.timer.TimerLinear
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.util.*

/**
 * ParticleEffects contain the data to draw particles using a specified animation and timer.<br></br>
 * They only run once, with a limited runtime, after being called. **They do not loop! They are just one-time effects.**<br></br>
 * For loops they must be wrapped in a [ParticleAnimation].<br></br>
 *
 *
 *
 * The [Timer] handles the actual runtime of the effect.<br></br>
 * Each type of Timer has a start and stop value, that specifies the duration.<br></br>
 * The increments can further stretch/shorten the duration, by increasing/decreasing the steps.
 *
 *
 *
 *
 * The [Animator] is linked to the timer and uses it's state to draw the particles (like a shape) dependent on it.<br></br>
 * It is what actually spawns the particles and makes use of the set data.
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@OptionalKeyReference(field = "key")
class ParticleEffect : Keyed {
    @JsonIgnore
    private var key: Key? = null

    val name: String
    val description: List<String>
    val icon: Material

    @JsonIgnore
    val dataType: Class<*>
    val particle: Particle

    @get:JsonGetter("data")
    var data: Any? = null
    var offset: Vector = Vector(0, 0, 0)
    var count: Int = 1

    @set:JsonAlias("speed")
    var extra: Double = 0.0

    @get:JsonGetter("timer")
    @set:JsonSetter("timer")
    var timeSupplier: Timer = TimerLinear()
    var animator: Animator? = null

    @JsonCreator
    constructor(@JsonProperty("particle") particle: Particle) {
        this.name = ""
        this.description = listOf()
        this.icon = Material.FIREWORK_STAR
        this.particle = particle
        this.dataType = particle.dataType
    }

    constructor(
        particle: Particle,
        count: Int,
        offset: Vector,
        extra: Double,
        data: Any?,
        timer: Timer,
        animator: Animator
    ) {
        this.name = ""
        this.description = listOf()
        this.icon = Material.FIREWORK_ROCKET

        this.particle = particle
        this.dataType = particle.dataType
        this.count = count
        this.offset = offset
        this.extra = extra
        if (data != null) {
            Preconditions.checkArgument(
                dataType != Void.TYPE && dataType.isInstance(data),
                "Invalid type of data! Expected " + dataType.name + " but got " + data.javaClass.name
            )
        }
        this.data = data
        this.timeSupplier = Objects.requireNonNullElse(timer, TimerLinear())
        this.animator = Objects.requireNonNullElse(animator, AnimatorBasic())
    }

    @Deprecated("")
    constructor(particle: Particle, count: Int, extra: Double, data: Any?, offset: Vector) {
        this.name = ""
        this.description = listOf()
        this.icon = Material.FIREWORK_ROCKET

        this.particle = particle
        this.dataType = particle.dataType
        this.count = count
        this.offset = offset
        this.extra = extra
        this.data = data
        this.timeSupplier = TimerLinear()
        this.animator = AnimatorBasic()
    }

    @JsonIgnore
    override fun key(): Key {
        return key!!
    }

    fun setKey(key: Key?) {
        this.key = key
    }

    fun hasKey(): Boolean {
        return key != null
    }

    @JsonSetter("data")
    private fun setDataFromJson(data: JsonNode) {
        if (dataType != Void.TYPE) {
            this.data = objectMapper.convertValue(data, dataType)
            Preconditions.checkArgument(
                this.data != null,
                "ParticleEffect requires data! Expected instance of " + dataType.name + " but got null!"
            )
        }
    }

    override fun toString(): String {
        return "ParticleEffect{" +
                "key=" + key +
                ", name='" + name + '\'' +
                ", description=" + description +
                ", icon=" + icon +
                ", dataType=" + dataType +
                ", particle=" + particle +
                ", data=" + data +
                ", offset=" + offset +
                ", count=" + count +
                ", extra=" + extra +
                ", timer=" + timeSupplier +
                ", animator=" + animator +
                '}'
    }

    /**
     * Spawns the effect at the specified location.
     * Particles are sent to all players in range.
     *
     * @param location The location at which the effect is spawned at.
     */
    fun spawn(location: Location) {
        Task(location).run()
    }

    /**
     * Spawns the effect at the specified location. Particles are sent to all players in range.<br></br>
     * The [ParticlePos] allows for a variable location target.
     *
     * @param location The location to spawn the effect at. Might be a variable target.
     */
    fun spawn(location: ParticlePos) {
        Task(location).run()
    }

    /**
     * Spawns the effect at the specified location.
     * If the player is specified, the particles are only send to that player.
     *
     * @param location The location at which the effect is spawned at.
     * @param player   The optional player to send the particles to.
     */
    fun spawn(location: Location, player: Player? = null) {
        Task(location, player).run()
    }

    fun spawn(location: ParticlePos, player: Player? = null) {
        Task(location, player).run()
    }

    fun spawn(block: Block) {
        Task(block.location).run()
    }

    fun spawn(entity: Entity) {
        Task(entity.location).run()
    }

    /**
     * Task that executes the particle effect and runs it in a new thread.
     */
    inner class Task : Runnable {
        private val player: Player?
        private val origin: ParticlePos
        private val runner = timeSupplier.createRunner()

        @JvmOverloads
        constructor(origin: Location, player: Player? = null) {
            this.player = player
            this.origin = ParticlePosLocation(origin)
        }

        @JvmOverloads
        constructor(origin: ParticlePos, player: Player? = null) {
            this.player = player
            this.origin = origin
        }

        override fun run() {
            get().scheduler.task(get().corePlugin)
                .delay(0)
                .interval(1)
                .execute {
                    animator!!.draw(
                        runner, this@ParticleEffect,
                        origin.location!!, player
                    )
                    if (runner.shouldStop()) {
                        this@execute.cancel()
                    }
                }
                .build()
        }
    }
}
