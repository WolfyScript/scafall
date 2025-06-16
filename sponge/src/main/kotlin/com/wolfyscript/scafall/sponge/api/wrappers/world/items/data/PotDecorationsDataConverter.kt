package com.wolfyscript.scafall.sponge.api.wrappers.world.items.data

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.sponge.api.data.SpongeItemStackDataComponentConverter
import com.wolfyscript.scafall.adventure.toAPI
import org.spongepowered.api.ResourceKey
import org.spongepowered.api.data.Keys
import org.spongepowered.api.item.ItemType
import org.spongepowered.api.item.ItemTypes
import kotlin.jvm.optionals.getOrNull

internal val potDecorationsDataConverter = SpongeItemStackDataComponentConverter<List<Key>>({
    if (this.type() == ItemTypes.DECORATED_POT) {
        return@SpongeItemStackDataComponentConverter buildList {
            get(Keys.POT_FRONT_DECORATION).map { ItemTypes.registry().findValueKey(it).getOrNull()?.toAPI() }.orElseGet { Key.defaultKey("brick") }
            get(Keys.POT_RIGHT_DECORATION).map { ItemTypes.registry().findValueKey(it).getOrNull()?.toAPI() }.orElseGet { Key.defaultKey("brick") }
            get(Keys.POT_LEFT_DECORATION).map { ItemTypes.registry().findValueKey(it).getOrNull()?.toAPI() }.orElseGet { Key.defaultKey("brick") }
            get(Keys.POT_BACK_DECORATION).map { ItemTypes.registry().findValueKey(it).getOrNull()?.toAPI() }.orElseGet { Key.defaultKey("brick") }
        }
    }
    null
}, {
    if (this.type() == ItemTypes.DECORATED_POT) {
        offer(Keys.POT_FRONT_DECORATION, ItemTypes.registry().findValue<ItemType>(ResourceKey.resolve(it[0].toString())).orElseGet { ItemTypes.BRICK.get() })
        offer(Keys.POT_RIGHT_DECORATION, ItemTypes.registry().findValue<ItemType>(ResourceKey.resolve(it[1].toString())).orElseGet { ItemTypes.BRICK.get() })
        offer(Keys.POT_LEFT_DECORATION, ItemTypes.registry().findValue<ItemType>(ResourceKey.resolve(it[2].toString())).orElseGet { ItemTypes.BRICK.get() })
        offer(Keys.POT_BACK_DECORATION, ItemTypes.registry().findValue<ItemType>(ResourceKey.resolve(it[3].toString())).orElseGet { ItemTypes.BRICK.get() })
    }
})