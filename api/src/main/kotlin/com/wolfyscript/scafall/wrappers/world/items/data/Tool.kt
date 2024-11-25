package com.wolfyscript.scafall.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key

interface Tool {

    val defaultMiningSpeed: Float

    val damagePerBlock: Int

    val rules: List<Rule>

    interface Rule {

        val blocks : List<Key>

        val speed: Float?

        val correctForDrops : Boolean?

    }

}