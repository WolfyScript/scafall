package com.wolfyscript.scafall.common.api.wrappers.world.items.data

import com.wolfyscript.scafall.common.api.data.MinecraftItemStackDataComponentConverter
import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.wrappers.world.items.data.TooltipDisplay
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation

val tooltipDisplayConverter =
    MinecraftItemStackDataComponentConverter(DataComponents.TOOLTIP_DISPLAY, { TooltipDisplayImpl(this) })

class TooltipDisplayImpl(val tooltipDisplay: net.minecraft.world.item.component.TooltipDisplay?) : TooltipDisplay, MinecraftDataComponentWrapper<TooltipDisplay, net.minecraft.world.item.component.TooltipDisplay> {

    constructor(
        hideTooltip: Boolean = false,
        hiddenComponents: Set<Key> = mutableSetOf(),
    ) : this(null) {
        this.hideTooltips = hideTooltip
        this.hiddenComponents = hiddenComponents.toMutableSet()
    }

    private var wrappedHiddenComponents = lazy {
        tooltipDisplay?.hiddenComponents?.mapNotNull {
            BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(it)?.let { key -> Key.key(key.namespace, key.path) }
        }?.toMutableSet() ?: mutableSetOf()
    }

    /**
     * Modifiable properties
     */

    override var hideTooltips: Boolean = tooltipDisplay?.hideTooltip() ?: false

    override var hiddenComponents: MutableSet<Key>
        get() = wrappedHiddenComponents.value
        set(value) {
            wrappedHiddenComponents = lazy { value }
        }

    override fun toMinecraft(): net.minecraft.world.item.component.TooltipDisplay {
        return net.minecraft.world.item.component.TooltipDisplay(
            hideTooltips,
            if (wrappedHiddenComponents.isInitialized()) {
                ReferenceLinkedOpenHashSet<DataComponentType<*>>().apply {
                    for (key in wrappedHiddenComponents.value) {
                        BuiltInRegistries.DATA_COMPONENT_TYPE.get(
                            ResourceLocation.fromNamespaceAndPath(
                                key.namespace,
                                key.value
                            )
                        )
                    }
                }
            } else {
                tooltipDisplay?.hiddenComponents ?: ReferenceLinkedOpenHashSet()
            }
        )
    }
}

interface MinecraftDataComponentWrapper<W: Any, I: Any> {

    fun toMinecraft(): I

}

