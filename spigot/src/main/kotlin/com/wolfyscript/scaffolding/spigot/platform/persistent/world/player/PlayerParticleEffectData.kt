package com.wolfyscript.scaffolding.spigot.platform.persistent.world.player

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.identifier.Key.Companion.scaffoldingKey
import com.wolfyscript.scaffolding.identifier.StaticNamespacedKey
import com.wolfyscript.scaffolding.spigot.platform.particles.ParticleUtils
import org.bukkit.inventory.EquipmentSlot
import java.util.*

@StaticNamespacedKey(value = "wolfyutilities:particles/effects")
class PlayerParticleEffectData : CustomPlayerData(ID) {
    private val effectsPerSlot: MutableMap<EquipmentSlot, UUID> = EnumMap(
        EquipmentSlot::class.java
    )

    override fun onLoad() {
    }

    override fun onUnload() {
    }

    fun hasActiveItemEffects(equipmentSlot: EquipmentSlot): Boolean {
        return effectsPerSlot.containsKey(equipmentSlot)
    }

    @get:JsonIgnore
    val activeItemEffects: Map<EquipmentSlot, UUID>
        /**
         * Gets the particle effects that are currently active on the player.
         *
         * @return The active particle effects on the player
         */
        get() = EnumMap(effectsPerSlot)

    fun getActiveItemEffects(equipmentSlot: EquipmentSlot): UUID? {
        return effectsPerSlot[equipmentSlot]
    }

    fun setActiveParticleEffect(equipmentSlot: EquipmentSlot, effectUUID: UUID) {
        stopActiveParticleEffect(equipmentSlot)
        effectsPerSlot[equipmentSlot] = effectUUID
    }

    fun stopActiveParticleEffect(equipmentSlot: EquipmentSlot) {
        ParticleUtils.stopAnimation(getActiveItemEffects(equipmentSlot))
        effectsPerSlot.remove(equipmentSlot)
    }

    override fun copy(): CustomPlayerData? {
        return null
    }

    companion object {
        val ID: Key = scaffoldingKey("particles/effects")
    }
}
