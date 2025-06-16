package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.sponge.api.data.SpongeItemStackDataComponentConverter
import com.wolfyscript.scafall.adventure.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.CanPlaceOn
import org.spongepowered.api.ResourceKey
import org.spongepowered.api.block.BlockType
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.data.Keys
import kotlin.jvm.optionals.getOrNull

val canPlaceOnDataConverter = SpongeItemStackDataComponentConverter({
    val keyList = get(Keys.PLACEABLE_BLOCK_TYPES).map { keys ->
        keys.mapNotNull {
            BlockTypes.registry().findValueKey(it).getOrNull()?.toAPI()
        }
    }.orElse(emptyList())

    CanPlaceOn(keyList)
}, {
    val blockTypes = it.blocks.mapNotNull { key -> BlockTypes.registry().findValue<BlockType>(ResourceKey.resolve(key.toString())).getOrNull() }.toSet()
    offer(Keys.PLACEABLE_BLOCK_TYPES, blockTypes)
})
