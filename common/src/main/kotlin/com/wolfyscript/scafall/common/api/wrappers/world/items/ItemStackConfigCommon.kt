package com.wolfyscript.scafall.common.api.wrappers.world.items

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonSetter
import com.wolfyscript.scafall.eval.value_provider.ValueProvider
import com.wolfyscript.scafall.eval.value_provider.ValueProviderIntegerConst
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot

abstract class ItemStackConfigCommon(
    override val stack: ItemStackSnapshot
) : ItemStackConfig {

    override var amount: ValueProvider<Int> = ValueProviderIntegerConst(1)

    override var overrides: Map<Key, ItemStackConfig.Override> = emptyMap()

    @JsonGetter("overrides")
    private fun parseOverrides(override: Collection<ItemStackConfig.Override>) {
        overrides = override.associateBy { it.type }
    }

    @JsonSetter("overrides")
    private fun writeOverrides() : Collection<ItemStackConfig.Override> {
        return overrides.values
    }

}