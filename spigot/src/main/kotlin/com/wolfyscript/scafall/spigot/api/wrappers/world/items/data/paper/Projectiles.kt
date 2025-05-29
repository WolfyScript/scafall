package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.items.data.ChargedProjectiles
import com.wolfyscript.scafall.wrappers.world.items.data.IntangibleProjectile
import io.papermc.paper.datacomponent.DataComponentTypes

internal val chargedProjectilesConverter = PaperDataAPIConverter(
    {
        val projectiles = unwrap().getData(DataComponentTypes.CHARGED_PROJECTILES)
        if (projectiles == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        return@PaperDataAPIConverter Result.success(ChargedProjectiles(projectiles.projectiles().map { it.wrap() }))
    }, {
        unwrap().setData(DataComponentTypes.CHARGED_PROJECTILES, io.papermc.paper.datacomponent.item.ChargedProjectiles.chargedProjectiles(it.projectiles.map { it.unwrap() }))
        return@PaperDataAPIConverter Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.CHARGED_PROJECTILES)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)

internal val intangibleProjectileConverter = PaperDataAPIConverter(
    {
        Result.success(if (unwrap().hasData(DataComponentTypes.INTANGIBLE_PROJECTILE)) {
            IntangibleProjectile() } else { null })
    }, {
        unwrap().setData(DataComponentTypes.INTANGIBLE_PROJECTILE)
        Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.INTANGIBLE_PROJECTILE)
        Result.success(this to true)
    }
)