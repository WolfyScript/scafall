package com.wolfyscript.scaffolding.spigot.api.wrappers.world.items.data

import com.wolfyscript.scaffolding.function.ReceiverBiConsumer
import com.wolfyscript.scaffolding.function.ReceiverFunction
import org.bukkit.inventory.meta.ItemMeta

data class ItemMetaDataKeyConverter<T: Any>(val fetcher: ReceiverFunction<ItemMeta, T?>, val applier: ReceiverBiConsumer<ItemMeta, T>)
