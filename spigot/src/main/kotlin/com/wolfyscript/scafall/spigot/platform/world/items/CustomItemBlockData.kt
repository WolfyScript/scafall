package com.wolfyscript.scafall.spigot.platform.world.items

import com.fasterxml.jackson.annotation.JacksonInject
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.Scafall
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.key
import com.wolfyscript.scafall.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scafall.spigot.platform.customItems
import com.wolfyscript.scafall.spigot.platform.particles.ParticleLocation
import com.wolfyscript.scafall.spigot.platform.particles.ParticleUtils.stopAnimation
import com.wolfyscript.scafall.spigot.platform.persistent.events.BlockStorageBreakEvent
import com.wolfyscript.scafall.spigot.platform.persistent.events.BlockStoragePlaceEvent
import com.wolfyscript.scafall.spigot.platform.persistent.world.BlockStorage
import com.wolfyscript.scafall.spigot.platform.persistent.world.ChunkStorage
import com.wolfyscript.scafall.spigot.platform.persistent.world.CustomBlockData
import com.wolfyscript.scafall.spigot.platform.world.items.events.CustomItemBreakEvent
import org.bukkit.Bukkit
import org.bukkit.util.Vector
import java.util.*

class CustomItemBlockData : CustomBlockData {
    @JsonIgnore
    private val core: Scafall

    @JsonIgnore
    private val chunkStorage: ChunkStorage

    @JsonIgnore
    private val pos: Vector

    @JsonIgnore
    private var particleAnimationID: UUID?

    val item: Key

    @JsonCreator
    constructor(
        @JacksonInject core: Scafall,
        @JacksonInject chunkStorage: ChunkStorage,
        @JacksonInject pos: Vector,
        @JsonProperty("item") item: Key
    ) : super(
        ID
    ) {
        this.core = core
        this.chunkStorage = chunkStorage
        this.pos = pos
        this.item = item
        this.particleAnimationID = null
    }

    private constructor(other: CustomItemBlockData) : super(ID) {
        this.core = other.core
        this.chunkStorage = other.chunkStorage
        this.pos = other.pos
        this.item = key(other.key().namespace, other.key().value)
        this.particleAnimationID = null
    }

    @get:JsonIgnore
    val customItem: Optional<CustomItem>
        get() = Optional.ofNullable(core.registries.customItems[item])

    @get:JsonIgnore
    val animation: Optional<UUID>
        get() = Optional.of(particleAnimationID!!)

    fun setParticleAnimationID(particleAnimationID: UUID) {
        this.particleAnimationID = particleAnimationID
    }

    fun onPlace(event: BlockStoragePlaceEvent) {
        customItem.ifPresent { customItem: CustomItem ->
            val animation =
                customItem.particleContent.getAnimation(ParticleLocation.BLOCK)
            if (animation != null) {
                setParticleAnimationID(animation.spawn(event.blockPlaced))
            }
        }
    }

    fun onBreak(event: BlockStorageBreakEvent) {
        customItem.ifPresent { customItem ->
            val event1 = CustomItemBreakEvent(customItem, event)
            Bukkit.getPluginManager().callEvent(event1)
            event.isCancelled = event1.isCancelled
        }
        stopAnimation(particleAnimationID)
    }

    override fun onLoad() {
        customItem.ifPresent { customItem ->
            val animation =
                customItem.particleContent.getAnimation(ParticleLocation.BLOCK)
            if (animation != null) {
                setParticleAnimationID(
                    animation.spawn(
                        chunkStorage.chunk!!.world.getBlockAt(
                            pos.blockX,
                            pos.blockY,
                            pos.blockZ
                        )
                    )
                )
            }
        }
    }

    override fun onUnload() {
        stopAnimation(particleAnimationID)
    }

    override fun copy(): CustomItemBlockData {
        return CustomItemBlockData(this)
    }

    override fun copyTo(storage: BlockStorage): CustomItemBlockData {
        return CustomItemBlockData(core, storage.chunkStorage, storage.pos, item)
    }

    companion object {
        val ID: Key = scaffoldingKey("custom_item")
    }
}
