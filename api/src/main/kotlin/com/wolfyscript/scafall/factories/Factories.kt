package com.wolfyscript.scafall.factories

/**
 * Factories are used to create instances of types specified in the API. Those types may have platform-specific implementations or are implemented in the common module.
 */
interface Factories {

    val itemsFactory: ItemsFactory

    val registryFactory: RegistryFactory
}