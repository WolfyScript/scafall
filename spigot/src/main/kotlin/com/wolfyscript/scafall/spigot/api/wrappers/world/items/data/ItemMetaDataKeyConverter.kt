package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data

import com.wolfyscript.scafall.function.ReceiverBiConsumer
import com.wolfyscript.scafall.function.ReceiverFunction
import org.bukkit.inventory.meta.ItemMeta

data class ItemMetaDataKeyConverter<T: Any>(val fetcher: ReceiverFunction<ItemMeta, T?>, val applier: ReceiverBiConsumer<ItemMeta, T>)
