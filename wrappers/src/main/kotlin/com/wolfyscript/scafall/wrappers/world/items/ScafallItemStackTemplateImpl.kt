package com.wolfyscript.scafall.wrappers.world.items

import net.minecraft.world.item.ItemStackTemplate

@JvmInline
internal value class ScafallItemStackTemplateImpl private constructor(
    val template: ItemStackTemplate,
) : ScafallItemStackTemplate {

    override fun unwrap(): ItemStackTemplate {
        return template
    }

    companion object {

        fun wrap(template: ItemStackTemplate): ScafallItemStackTemplateImpl {
            return ScafallItemStackTemplateImpl(template)
        }

    }

}