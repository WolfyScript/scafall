package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.sponge.api.data.SpongeItemStackDataComponentConverter
import com.wolfyscript.scafall.sponge.api.wrappers.unwrap
import com.wolfyscript.scafall.sponge.api.wrappers.wrap
import com.wolfyscript.scafall.wrappers.world.items.data.ChargedProjectiles
import com.wolfyscript.scafall.wrappers.world.items.data.IntangibleProjectile
import org.spongepowered.api.data.Keys
import kotlin.jvm.optionals.getOrNull

val chargedProjectilesDataConverter = SpongeItemStackDataComponentConverter({
    get(Keys.CHARGED_PROJECTILES).map {
        ChargedProjectiles(it.map { stack -> stack.asMutableCopy().wrap() })
    }.getOrNull()
}, {
    offer(Keys.CHARGED_PROJECTILES, it.projectiles.map { stack -> stack.unwrap().asImmutable() })
})

val intangibleProjectileDataConverter = SpongeItemStackDataComponentConverter({
    if (get(Keys.INTANGIBLE_PROJECTILE).orElse(false)){
        IntangibleProjectile()
    } else null
}, {
    offer(Keys.INTANGIBLE_PROJECTILE, true)
})
