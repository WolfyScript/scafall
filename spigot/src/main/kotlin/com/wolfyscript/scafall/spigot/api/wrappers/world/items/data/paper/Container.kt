package com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.paper

import com.wolfyscript.scafall.spigot.api.data.PaperDataAPIConverter
import com.wolfyscript.scafall.spigot.api.wrappers.unwrap
import com.wolfyscript.scafall.spigot.api.wrappers.world.items.data.ContainerImpl
import com.wolfyscript.scafall.spigot.api.wrappers.wrap
import com.wolfyscript.scafall.toAPI
import com.wolfyscript.scafall.wrappers.world.items.data.BundleContents
import com.wolfyscript.scafall.wrappers.world.items.data.Container
import com.wolfyscript.scafall.wrappers.world.items.data.ContainerLoot
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemContainerContents
import io.papermc.paper.datacomponent.item.SeededContainerLoot

internal val bundleContentsConverter = PaperDataAPIConverter(
    {
        val contents = unwrap().getData(DataComponentTypes.BUNDLE_CONTENTS)
        if (contents == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        return@PaperDataAPIConverter Result.success(BundleContents(contents.contents().map { it.wrap() }))
    }, {
        unwrap().setData(DataComponentTypes.BUNDLE_CONTENTS, io.papermc.paper.datacomponent.item.BundleContents.bundleContents(it.contents.map { it.unwrap() }))
        return@PaperDataAPIConverter Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.BUNDLE_CONTENTS)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)


internal val containerConverter = PaperDataAPIConverter<Container>(
    {
        val container = unwrap().getData(DataComponentTypes.CONTAINER)
        if (container == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        return@PaperDataAPIConverter Result.success(ContainerImpl(container.contents().map {
            it.wrap()
        }))
    }, {
        unwrap().setData(DataComponentTypes.CONTAINER, ItemContainerContents.containerContents(it.contents.map { it.unwrap() }))
        return@PaperDataAPIConverter Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.CONTAINER)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)

internal val containerLootConverter = PaperDataAPIConverter(
    {
        val containerLoot = unwrap().getData(DataComponentTypes.CONTAINER_LOOT)
        if (containerLoot == null) {
            return@PaperDataAPIConverter Result.success(null)
        }
        return@PaperDataAPIConverter Result.success(ContainerLoot(containerLoot.lootTable().toAPI(), containerLoot.seed()))
    }, {
        unwrap().setData(DataComponentTypes.CONTAINER_LOOT, SeededContainerLoot.seededContainerLoot(it.lootTable.into(), it.seed))
        return@PaperDataAPIConverter Result.success(this)
    }, {
        unwrap().unsetData(DataComponentTypes.CONTAINER_LOOT)
        return@PaperDataAPIConverter Result.success(this to true)
    }
)