package com.wolfyscript.scafall.items

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonSetter
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.eval.value_provider.ValueProviderIntegerConst
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot

/**
 * A template configuration for an ItemStack.
 * This class provides a base implementation for handling basic properties of an ItemStack,
 * such as its amount and overrides.
 *
 * @property stack The initial ItemStackSnapshot.
 */
@Deprecated("Still work in progress, not ready for use")
abstract class ItemStackConfigCommon(
    override val stack: ItemStackSnapshot
) : ItemStackConfig {

    /**
     * The amount of the item stack. This can be a dynamic value provided by a ValueProvider.
     * Defaults to 1 if not specified.
     *
     * @see ValueProvider
     */
    override var amount: ValueProvider<Int> = ValueProviderIntegerConst(1)

    /**
     * A map of overrides for the item stack. Each override is identified by a Key and
     * contains a specific configuration for that item.
     *
     * @see Override
     * @see Key
     */
    override var overrides: Map<Key, ItemStackConfig.Override> = emptyMap()

    @JsonGetter("overrides")
    private fun parseOverrides(override: Collection<ItemStackConfig.Override>) {
        // TODO: Implement parsing logic for overrides
    }

    @JsonSetter("overrides")
    private fun writeOverrides() : Collection<ItemStackConfig.Override> {
        return overrides.values
    }

}