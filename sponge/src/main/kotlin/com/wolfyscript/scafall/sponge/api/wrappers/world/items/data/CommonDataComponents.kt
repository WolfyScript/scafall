package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.sponge.api.data.ItemStackDataKeyConverter
import com.wolfyscript.scafall.wrappers.world.items.data.ItemLore
import com.wolfyscript.scafall.wrappers.world.items.data.Unbreakable
import org.spongepowered.api.data.Keys
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

val unbreakableDataConverter = ItemStackDataKeyConverter({
    if (get(Keys.IS_UNBREAKABLE).getOrNull() == true) {
        return@ItemStackDataKeyConverter Unbreakable(get(Keys.HIDE_UNBREAKABLE).getOrNull() ?: false)
    }
    null
}, {
    offer(Keys.IS_UNBREAKABLE, true)
    offer(Keys.HIDE_UNBREAKABLE, it.showInTooltip)
})

val damageConverter = ItemStackDataKeyConverter({
    get(Keys.ITEM_DURABILITY).map {
        get(Keys.MAX_DURABILITY).getOrElse { 0 } - it
    }.getOrNull()
}, { damage ->
    get(Keys.MAX_DURABILITY).ifPresent { max ->
        offer(Keys.ITEM_DURABILITY, max - damage)
    }
})

val displayNameConverter = ItemStackDataKeyConverter({ get(Keys.DISPLAY_NAME).getOrNull() }, { offer(Keys.DISPLAY_NAME, it) })
val displayLoreConverter = ItemStackDataKeyConverter({ ItemLore(get(Keys.LORE).getOrElse { emptyList() }) }, { offer(Keys.LORE, it.lines) })
val customModelDataConverter = ItemStackDataKeyConverter({
    get(Keys.CUSTOM_MODEL_DATA).getOrNull()
}, {
    offer(Keys.CUSTOM_MODEL_DATA, it)
})

