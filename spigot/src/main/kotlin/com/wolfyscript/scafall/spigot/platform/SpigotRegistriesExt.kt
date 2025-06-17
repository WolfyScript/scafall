package com.wolfyscript.scafall.spigot.platform

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.registry.*
import com.wolfyscript.scafall.spigot.api.nbt.QueryNode
import com.wolfyscript.scafall.spigot.platform.persistent.world.CustomBlockData
import com.wolfyscript.scafall.spigot.platform.persistent.world.player.CustomPlayerData

private const val errorNotRegistered: String = "Registries are not yet initialised!"

// persistent
private var customBlockDataRegistry: TypeRegistry<CustomBlockData>? = null
private var customPlayerDataRegistry: TypeRegistry<CustomPlayerData>? = null

// nbt
private var nbtQueriesRegistry: TypeRegistry<QueryNode<*>>? = null

val ScafallRegistries.customBlockData
    get() = customBlockDataRegistry ?: throw RuntimeException(errorNotRegistered)
val ScafallRegistries.customPlayerData
    get() = customPlayerDataRegistry ?: throw RuntimeException(errorNotRegistered)

// nbt
val ScafallRegistries.nbtQueries
    get() = nbtQueriesRegistry ?: throw RuntimeException(errorNotRegistered)


internal fun ScafallRegistries.registerSpigotPlatform() {
    customBlockDataRegistry = UniqueTypeRegistrySimple(Key.defaultKey("persistent/block"))
    customPlayerDataRegistry = UniqueTypeRegistrySimple(Key.defaultKey("persistent/player"))

    // nbt
    nbtQueriesRegistry = UniqueTypeRegistrySimple(Key.defaultKey("nbt/query/nodes"))
}