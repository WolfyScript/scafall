package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.common.api.wrappers.world.items.data.BlocksAttacksCommon
import com.wolfyscript.scafall.common.api.wrappers.world.items.data.WeaponCommon
import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.BlocksAttacks
import com.wolfyscript.scafall.wrappers.world.items.data.Weapon
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.tag.TagKey

internal val weaponConverter = PaperDataAPIConverter<Weapon>(
    {
        val paperWeapon = unwrap().getData(DataComponentTypes.WEAPON)
        if (paperWeapon == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        val weapon = WeaponCommon(paperWeapon.itemDamagePerAttack(), paperWeapon.disableBlockingForSeconds())
        return@PaperDataAPIConverter Result.success(weapon)
    }, {
        val paperWeapon = io.papermc.paper.datacomponent.item.Weapon.weapon().itemDamagePerAttack(it.itemDamagePerAttack).disableBlockingForSeconds(it.disableBlockingForSeconds)
        unwrap().setData(DataComponentTypes.WEAPON, paperWeapon)
        return@PaperDataAPIConverter Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.WEAPON)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)

internal val blocksAttacksConverter = PaperDataAPIConverter<BlocksAttacks>(
    {
        val paperObj = unwrap().getData(DataComponentTypes.BLOCKS_ATTACKS)
        if (paperObj == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        val blocksAttacks = BlocksAttacksCommon(
            paperObj.blockDelaySeconds(),
            paperObj.disableCooldownScale(),
            damageReductions = mutableListOf(),
            itemDamage = null,
            SoundEvent(paperObj.blockSound().key().toAPI()),
            SoundEvent(paperObj.disableSound().key().toAPI()),
            paperObj.bypassedBy().key().toAPI()
        )
        return@PaperDataAPIConverter Result.success(blocksAttacks)
    }, {
        val paperBlocksAttacks = io.papermc.paper.datacomponent.item.BlocksAttacks.blocksAttacks().apply {
            blockDelaySeconds(it.blockDelaySeconds)
            disableCooldownScale(it.disableCooldownScale)
            blockSound(it.blockSound.sound.into())
            disableSound(it.disabledSound.sound.into())
            bypassedBy(TagKey.create(RegistryKey.DAMAGE_TYPE, it.bypassedBy.into()))
        }
        unwrap().setData(DataComponentTypes.BLOCKS_ATTACKS, paperBlocksAttacks)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.BLOCKS_ATTACKS)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)