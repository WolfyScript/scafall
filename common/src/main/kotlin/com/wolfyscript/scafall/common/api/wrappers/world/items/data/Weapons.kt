package com.wolfyscript.scafall.common.api.wrappers.world.items.data

import com.wolfyscript.scafall.wrappers.world.items.data.Weapon

data class WeaponCommon(
    override var itemDamagePerAttack: Int,
    override var disableBlockingForSeconds: Float
) : Weapon
