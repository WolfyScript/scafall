package com.wolfyscript.scaffolding.spigot.platform

import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.registry.*
import com.wolfyscript.scaffolding.spigot.api.nbt.QueryNode
import com.wolfyscript.scaffolding.spigot.platform.particles.ParticleAnimation
import com.wolfyscript.scaffolding.spigot.platform.particles.animators.Animator
import com.wolfyscript.scaffolding.spigot.platform.particles.shapes.Shape
import com.wolfyscript.scaffolding.spigot.platform.particles.timer.Timer
import com.wolfyscript.scaffolding.spigot.platform.persistent.world.CustomBlockData
import com.wolfyscript.scaffolding.spigot.platform.persistent.world.player.CustomPlayerData
import com.wolfyscript.scaffolding.spigot.platform.registry.RegistryCustomItem
import com.wolfyscript.scaffolding.spigot.platform.registry.RegistryStackIdentifierParsers
import com.wolfyscript.scaffolding.spigot.platform.world.items.CustomItemData
import com.wolfyscript.scaffolding.spigot.platform.world.items.actions.Action
import com.wolfyscript.scaffolding.spigot.platform.world.items.actions.Event
import com.wolfyscript.scaffolding.spigot.platform.world.items.meta.Meta
import com.wolfyscript.scaffolding.spigot.platform.world.items.reference.StackIdentifier

private const val errorNotRegistered: String = "Registries are not yet initialised!"

private var stackIdentifierParsersRegistry: RegistryStackIdentifierParsers? = null
private var stackIdentifiersRegistry: TypeRegistry<StackIdentifier>? = null

// persistent
private var customBlockDataRegistry: TypeRegistry<CustomBlockData>? = null
private var customPlayerDataRegistry: TypeRegistry<CustomPlayerData>? = null

// particles
private var particleTimersRegistry: TypeRegistry<Timer>? = null
private var particleShapesRegistry: TypeRegistry<Shape>? = null
private var particleAnimatorsRegistry: TypeRegistry<Animator>? = null
private var particleAnimationsRegistry: Registry<ParticleAnimation>? = null

// custom items
private var customItemsRegistry: RegistryCustomItem? = null
private var customItemDataRegistry: TypeRegistry<CustomItemData>? = null
private var customItemActionsRegistry: TypeRegistry<Action<*>>? = null
private var customItemEventsRegistry: TypeRegistry<Event<*>>? = null
private var customItemDataChecksRegistry: TypeRegistry<Meta>? = null

// nbt
private var nbtQueriesRegistry: TypeRegistry<QueryNode<*>>? = null

val Registries.customBlockData
    get() = customBlockDataRegistry ?: throw RuntimeException(errorNotRegistered)
val Registries.customPlayerData
    get() = customPlayerDataRegistry ?: throw RuntimeException(errorNotRegistered)

//
val Registries.customItemData
    get() = customItemDataRegistry ?: throw RuntimeException(errorNotRegistered)
val Registries.customItemActions
    get() = customItemActionsRegistry ?: throw RuntimeException(errorNotRegistered)
val Registries.customItemEvents
    get() = customItemEventsRegistry ?: throw RuntimeException(errorNotRegistered)
val Registries.customItemDataChecks
    get() = customItemDataChecksRegistry ?: throw RuntimeException(errorNotRegistered)

//
val Registries.particleTimers
    get() = particleTimersRegistry ?: throw RuntimeException(errorNotRegistered)

val Registries.particleShapes
    get() = particleShapesRegistry ?: throw RuntimeException(errorNotRegistered)

val Registries.particleAnimators
    get() = particleAnimatorsRegistry ?: throw RuntimeException(errorNotRegistered)

//
val Registries.stackIdentifierParsers
    get() = stackIdentifierParsersRegistry ?: throw RuntimeException(errorNotRegistered)
val Registries.stackIdentifiers
    get() = stackIdentifiersRegistry ?: throw RuntimeException(errorNotRegistered)
val Registries.customItems
    get() = customItemsRegistry ?: throw RuntimeException(errorNotRegistered)

//
val Registries.particleAnimations : Registry<ParticleAnimation>
    get() = particleAnimationsRegistry ?: throw RuntimeException(errorNotRegistered)

// nbt
val Registries.nbtQueries
    get() = nbtQueriesRegistry ?: throw RuntimeException(errorNotRegistered)


internal fun Registries.registerSpigotPlatform() {
    stackIdentifierParsersRegistry = RegistryStackIdentifierParsers(this)
    stackIdentifiersRegistry = UniqueTypeRegistrySimple(Key.scaffoldingKey("stack_identifiers/types"), this)
    customItemsRegistry = RegistryCustomItem(this)

    customBlockDataRegistry = UniqueTypeRegistrySimple(Key.scaffoldingKey("persistent/block"), this)
    customPlayerDataRegistry = UniqueTypeRegistrySimple(Key.scaffoldingKey("persistent/player"), this)

    particleTimersRegistry = UniqueTypeRegistrySimple(Key.scaffoldingKey("particles/timers"), this)
    particleShapesRegistry = UniqueTypeRegistrySimple(Key.scaffoldingKey("particles/shapes"), this)
    particleAnimatorsRegistry = UniqueTypeRegistrySimple(Key.scaffoldingKey("particles/animators"), this)

    //
    particleAnimationsRegistry = RegistrySimple(Key.scaffoldingKey("particles/animations"), this)

    //
    customItemDataRegistry = UniqueTypeRegistrySimple(Key.scaffoldingKey("custom_items/data"), this)
    customItemActionsRegistry = UniqueTypeRegistrySimple(Key.scaffoldingKey("custom_items/actions"), this)
    customItemEventsRegistry = UniqueTypeRegistrySimple(Key.scaffoldingKey("custom_items/events"), this)
    customItemDataChecksRegistry = UniqueTypeRegistrySimple(Key.scaffoldingKey("custom_items/data_checks"), this)

    // nbt
    nbtQueriesRegistry = UniqueTypeRegistrySimple(Key.scaffoldingKey("nbt/query/nodes"), this)
}