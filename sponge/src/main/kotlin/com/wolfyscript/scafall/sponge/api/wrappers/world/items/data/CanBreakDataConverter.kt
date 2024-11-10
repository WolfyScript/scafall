package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.sponge.api.data.SpongeItemStackDataComponentConverter
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.CanBreak
import org.spongepowered.api.ResourceKey
import org.spongepowered.api.block.BlockType
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.data.Keys
import kotlin.jvm.optionals.getOrNull

val canBreakDataConverter = SpongeItemStackDataComponentConverter({
    val keyList = get(Keys.BREAKABLE_BLOCK_TYPES).map { keys ->
        keys.mapNotNull {
            BlockTypes.registry().findValueKey(it).getOrNull()?.toAPI()
        }
    }.orElse(emptyList())

    CanBreak(get(Keys.HIDE_CAN_DESTROY).orElse(false), keyList)
}, {
    offer(Keys.HIDE_CAN_DESTROY, !it.showInTooltip)
    val blockTypes = it.blocks.mapNotNull { key -> BlockTypes.registry().findValue<BlockType>(ResourceKey.resolve(key.toString())).getOrNull() }.toSet()
    offer(Keys.BREAKABLE_BLOCK_TYPES, blockTypes)
})