package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.common.api.wrappers.world.items.data.ToolImpl
import com.wolfyscript.scafall.sponge.api.data.SpongeItemStackDataComponentConverter
import com.wolfyscript.scafall.wrappers.world.items.data.DamageResistant
import com.wolfyscript.scafall.wrappers.world.items.data.Repairable
import com.wolfyscript.scafall.wrappers.world.items.data.Unbreakable
import org.spongepowered.api.data.Keys
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

internal val unbreakableDataConverter = SpongeItemStackDataComponentConverter({
    if (get(Keys.IS_UNBREAKABLE).getOrNull() == true) {
        return@SpongeItemStackDataComponentConverter Unbreakable()
    }
    null
}, {
    offer(Keys.IS_UNBREAKABLE, true)
}, {
    remove(Keys.IS_UNBREAKABLE)
})

internal val damageConverter = SpongeItemStackDataComponentConverter({
    get(Keys.ITEM_DURABILITY).map {
        get(Keys.MAX_DURABILITY).getOrElse { 0 } - it
    }.getOrNull()
}, { damage ->
    get(Keys.MAX_DURABILITY).ifPresent { max ->
        offer(Keys.ITEM_DURABILITY, max - damage)
    }
}, {
    offer(Keys.ITEM_DURABILITY, get(Keys.MAX_DURABILITY).getOrElse { 0 })
})

internal val repairCostConverter = SpongeItemStackDataComponentConverter({
    get(Keys.REPAIR_COST).getOrNull()
}, {
    offer(Keys.REPAIR_COST, it)
}, {
    remove(Keys.REPAIR_COST)
})

//internal val damageResistantConverter: SpongeItemStackDataComponentConverter<DamageResistant> = TODO("Not implemented")
//
//internal val repairableConverter: SpongeItemStackDataComponentConverter<Repairable> = TODO("Not implemented")
