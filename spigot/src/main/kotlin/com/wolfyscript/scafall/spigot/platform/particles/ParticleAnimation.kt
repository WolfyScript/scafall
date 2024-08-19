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

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.config.jackson.OptionalKeyReference
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Keyed
import com.wolfyscript.scafall.scheduling.Task
import com.wolfyscript.scafall.spigot.platform.particles.ParticleAnimation.ParticleEffectSettings
import com.wolfyscript.scafall.spigot.platform.particles.pos.*
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.util.Vector
import java.util.*
import java.util.stream.Collectors

/**
 * ParticleAnimations are used to combine [ParticleEffect]s and schedule them to create animations.<br></br>
 * If you want to just spawn a one time ParticleEffect use the methods of the [ParticleEffect] class instead.<br></br>
 * <br></br>
 * **Runtime settings**
 *
 *
 * The animation contains some settings that control it's runtime.<br></br>
 * The whole runtime is handled in ticks and has a fixed duration.<br></br>
 *
 *
 * **[.delay]** is the initial delay (in ticks) it waits for until it starts.<br></br>
 * **[.interval]** is the duration (in ticks) one interval takes. After that duration is hit the counter resets to 0.<br></br>
 * **[.repetitions]** describe how often the interval is repeated. A value lower than 0 means that it loops indefinitely until stopped.
 *
 *
 * <br></br>
 * **The "keyframe" settings**
 *
 *
 * The way it works is that the counter counts up each tick until it reaches the specified [.interval], that's when it goes back to 0. (And loops depending on the repetitions)<br></br><br></br>
 * For example, an interval of 10 means the total interval takes 10 ticks and the counter counts from 0 to 9.<br></br>
 * For each of these ticks you can specify a list of [ParticleEffectSettings].<br></br>
 * These settings contain the [ParticleEffect] they will spawn, the offset ([Vector]) from it's origin, and the tick ([Integer]) to spawn it at.
 *
 *
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@OptionalKeyReference(field = "key")
class ParticleAnimation : Keyed {
    @JsonIgnore
    private var key: Key? = null

    private val name: String
    private val description: List<String>
    private val icon: Material

    private val delay: Int
    private val interval: Int
    private val repetitions: Int
    private val effects: MutableMap<Int, List<ParticleEffectSettings>>

    @JsonCreator
    constructor(@JsonProperty("effects") effects: MutableMap<Int, List<ParticleEffectSettings>>) {
        this.name = ""
        this.description = ArrayList()
        this.icon = Material.FIREWORK_ROCKET
        this.delay = 0
        this.interval = 1
        this.repetitions = 1
        this.effects = effects
    }

    /**
     * @param icon           The Material as the icon.
     * @param name           The name of this animation.
     * @param description    The description of this animation.
     * @param delay          The delay before the particles are spawned.
     * @param interval       The interval in which the ParticleEffects are spawned. In ticks.
     * @param effectSettings The ParticleEffects that will be spawned by this animation.
     */
    constructor(
        icon: Material,
        name: String,
        description: List<String>,
        delay: Int,
        interval: Int,
        repetitions: Int,
        vararg effectSettings: ParticleEffectSettings
    ) {
        this.icon = icon
        this.effects = Arrays.stream(effectSettings).collect(
            Collectors.toMap(ParticleEffectSettings::tick,
                { settings: ParticleEffectSettings ->
                    val values: MutableList<ParticleEffectSettings> = ArrayList()
                    values.add(settings)
                    values
                },
                { list, list2 ->
                    list + list2
                }))
        this.name = name
        this.description = Objects.requireNonNullElse(description, ArrayList())
        this.delay = delay
        this.interval = interval
        this.repetitions = repetitions
    }

    /**
     * This constructor is used for backwards compatibility.<br></br>
     * Each [ParticleEffect] is mapped to a new [ParticleEffectSettings] instance.<br></br>
     * To keep the old behaviour of the effects offset it moves the [ParticleEffect] to the [ParticleEffectSettings].
     *
     */
    @Deprecated("This method is just backwards compatibility. Use {@link #ParticleAnimation(Material, String, List, int, int, int, ParticleEffectSettings...)} instead!")
    constructor(
        icon: Material,
        name: String,
        description: List<String>,
        delay: Int,
        interval: Int,
        vararg effects: ParticleEffect
    ) {
        this.icon = icon
        this.effects = Arrays.stream(effects)
            .map { effect -> ParticleEffectSettings(effect, effect.offset, 0) }
            .collect(
                Collectors.toMap(ParticleEffectSettings::tick,
                    { settings: ParticleEffectSettings ->
                        settings.effect.offset = Vector()
                        val values: MutableList<ParticleEffectSettings> = ArrayList()
                        values.add(settings)
                        values
                    },
                    { list, list2 ->
                        list + list2
                    })
            )
        this.name = name
        this.description = Objects.requireNonNullElse(description, ArrayList())
        this.delay = delay
        this.interval = interval
        this.repetitions = -1
    }

    /**
     * Spawn the animation at the specified location in the world.
     *
     * @param location The location to spawn the animation at.
     */
    fun spawn(location: Location): UUID {
        return Scheduler(location).start()
    }

    /**
     * Spawn the animation on the specified block.
     *
     * @param block The block to spawn the animation on.
     */
    fun spawn(block: Block): UUID {
        return Scheduler(block).start()
    }

    /**
     * Spawn the animation on the specified entity.
     *
     * @param entity The entity to spawn the animation on.
     */
    fun spawn(entity: Entity): UUID {
        return Scheduler(entity).start()
    }

    /**
     * Spawn the animation on the specified Player and Equipment Slot.
     *
     * @param player The [Player] to spawn the animation on.
     * @param slot   The [EquipmentSlot] this animation is spawned on.
     */
    fun spawn(player: Player, slot: EquipmentSlot?) {
//        PlayerUtils.setActiveParticleEffect(player, slot, Scheduler(player).start())
    }

    override fun key(): Key {
        return key!!
    }

    fun setKey(key: Key?) {
        this.key = key
    }

    override fun toString(): String {
        return "ParticleAnimation{" +
                "key=" + key +
                ", name='" + name + '\'' +
                ", description=" + description +
                ", icon=" + icon +
                ", delay=" + delay +
                ", interval=" + interval +
                ", repetitions=" + repetitions +
                ", effects=" + effects +
                '}'
    }

    /**
     * This object contains the settings for a "keyframe" of the [ParticleAnimation].
     * It contains the [ParticleEffect] to spawn, the fixed offset from the origin, and the tick at which these settings are used.
     */
    @JvmRecord
    data class ParticleEffectSettings(val effect: ParticleEffect, val offset: Vector, val tick: Int)

    /**
     * This scheduler runs the [ParticleAnimation]s with the specified delay and interval.<br></br>
     * If it is started it is saved in cache and is assigned a [UUID] (See [ParticleUtils]).<br></br>
     * <br></br>
     * You can stop the scheduler at any time using its corresponding [UUID] and the [ParticleUtils.stopAnimation].<br></br>
     * If a continues animation is spawned, it is required to stop it manually if no longer needed, as it... well would continue forever.
     */
    inner class Scheduler(private val pos: ParticlePos, private val receiver: Player?) :
        Runnable {
        private var task: Task? = null
        private var uuid: UUID? = null
        private var tick = 0
        private var loop = 0

        private var tickSinceLastCheck = 0
        private var spawnEffects = true

        private val cachedOffsetPos: MutableMap<ParticleEffectSettings, ParticlePos?> = HashMap()

        /**
         * @param location The location to spawn the animation at.
         * @param receiver   The player to send the particles to. If null particles are sent to all surrounding players.
         */
        @JvmOverloads
        constructor(location: Location, receiver: Player? = null) : this(ParticlePosLocation(location), receiver)

        @JvmOverloads
        constructor(block: Block, receiver: Player? = null) : this(ParticlePosBlock(block), receiver)

        @JvmOverloads
        constructor(
            entity: Entity,
            receiver: Player? = null
        ) : this(if (entity is Player) ParticlePosPlayer(entity) else ParticlePosEntity(entity), receiver)

        /**
         * Starts and caches the animation.
         * It may be stopped if it is a continues animations, or before it is stopped automatically if it is limited.
         *
         * @return The UUID of the running animation.
         */
        fun start(): UUID {
            this.task = ScafallProvider.get().scheduler.syncTimerTask(
                ScafallProvider.get().corePlugin,
                this, delay.toLong(), 1
            )
            this.uuid = ParticleUtils.addScheduler(this)
            return uuid!!
        }

        /**
         * Stops the current running animation.
         */
        fun stop() {
            task?.cancel()
            ParticleUtils.removeScheduler(uuid)
            this.uuid = null
            this.task = null
        }

        val isRunning: Boolean
            get() = task != null // TODO && //!task.isCancelled();

        /**
         * This checks if the location is valid to spawn the effects and make more resource intensive calculations.
         * The spawn location is valid if it still exists and there are players nearby (64 block range).
         *
         *
         * The delay between checks is currently 80 ticks (4 seconds).
         */
        fun checkSpawnConditions(): Boolean {
            if (tickSinceLastCheck > 80) {
                val loc = pos.location
                if (loc != null && loc.world != null) {
                    val entities = loc.world.getNearbyEntities(
                        loc, 32.0, 32.0, 32.0
                    ) { entity1: Entity? -> entity1 is Player }
                    this.spawnEffects = !entities.isEmpty()
                } else {
                    this.spawnEffects = false
                }
                tickSinceLastCheck = 0
            } else {
                tickSinceLastCheck++
            }
            return spawnEffects
        }

        /**
         * This method contains the actual logic to spawn the particle effects.
         * It increases the counter and makes sure to only spawn effects if required.
         */
        protected fun execute() {
            if (tick >= interval) {
                tick = 0
                return
            }
            if (checkSpawnConditions()) {
                //Spawn tick specific ParticleEffects
                for (setting in effects.computeIfAbsent(tick) { ArrayList() }) {
                    cachedOffsetPos.computeIfAbsent(setting) { settings ->
                        val particlePos = pos.shallowCopy()
                        particlePos!!.offset = settings.offset
                        particlePos
                    }?.let { setting.effect.spawn(it, receiver) }
                }
            }
            tick++
        }

        /**
         * The core of the scheduler. This is actually executed each tick by the thread handling it.
         */
        override fun run() {
            if (repetitions <= -1 || loop < repetitions) {
                execute()
                if (tick == 0 && repetitions >= 0) {
                    loop++
                }
            } else {
                stop()
            }
        }
    }
}
