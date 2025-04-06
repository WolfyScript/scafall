package com.wolfyscript.scafall.common.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.data.BlocksAttacks
import com.wolfyscript.scafall.wrappers.world.items.data.Weapon
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent

data class WeaponCommon(
    override var itemDamagePerAttack: Int,
    override var disableBlockingForSeconds: Float
) : Weapon

data class BlocksAttacksCommon(
    override var blockDelaySeconds: Float,
    override var disableCooldownScale: Float,
    override val damageReductions: MutableList<BlocksAttacks.DamageReduction>,
    override var itemDamage: BlocksAttacks.ItemDamage?,
    override var blockSound: SoundEvent,
    override var disabledSound: SoundEvent,
    override var bypassedBy: Key
) : BlocksAttacks {

    data class DamageReduction(
        override var type: Key,
        override var base: Float,
        override var horizontalBlockingAngle: Float
    ) : BlocksAttacks.DamageReduction

    data class ItemDamage(
        override var threshold: Float,
        override var base: Float,
        override var factor: Float
    ) : BlocksAttacks.ItemDamage

}