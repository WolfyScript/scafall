package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.spigot.api.data.ItemMetaDataKeyConverter
import com.wolfyscript.scafall.spigot.api.identifiers.bukkit
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.Equippable
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent
import org.bukkit.Registry
import org.bukkit.inventory.EquipmentSlot

internal val equippableItemMetaConverter = ItemMetaDataKeyConverter<Equippable>(
    {
        if (hasEquippable()) {
            val currentEquippable = equippable
            return@ItemMetaDataKeyConverter Equippable(
                currentEquippable.slot.toString(), // TODO
                SoundEvent(currentEquippable.equipSound.key.toAPI()),
                currentEquippable.model?.toAPI(),
                currentEquippable.allowedEntities?.map { type -> type.key.toAPI() },
                currentEquippable.isDispensable,
                currentEquippable.isSwappable,
                currentEquippable.isDamageOnHurt,
                currentEquippable.cameraOverlay?.toAPI(),
            )
        }
        null
    },
    {
        if (it == null) {
            setEquippable(null)
            return@ItemMetaDataKeyConverter
        }
        val newEquippable = equippable

        newEquippable.slot = EquipmentSlot.valueOf(it.slot) // TODO
        newEquippable.model = it.assetId?.bukkit()
        Registry.SOUNDS.get(it.equipSound.sound.bukkit())?.let { sound ->
            newEquippable.setEquipSound(sound)
        }
        newEquippable.allowedEntities = it.allowedEntities?.mapNotNull { key ->
            Registry.ENTITY_TYPE.get(key.bukkit())
        }
        newEquippable.isDispensable = it.dispensable
        newEquippable.isSwappable = it.swappable
        newEquippable.isDamageOnHurt = it.damageOnHurt
        newEquippable.cameraOverlay = it.cameraOverlay?.bukkit()

        setEquippable(newEquippable)
    }
)