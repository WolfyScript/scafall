package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.sound.SoundEvent

data class BlocksAttacks(
    var blockDelaySeconds: Float,
    var disableCooldownScale: Float,
    val damageReductions: MutableList<DamageReduction>,
    var itemDamage: ItemDamage?,
    var blockSound: SoundEvent,
    var disabledSound: SoundEvent,
    var bypassedBy: Key
) {

    data class DamageReduction(
        var type: Key,
        var base: Float,
        var horizontalBlockingAngle: Float
    )

    data class ItemDamage(
        var threshold: Float,
        var base: Float,
        var factor: Float,
    )

}