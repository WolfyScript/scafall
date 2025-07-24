package com.wolfyscript.scafall.spigot.compat.denizen

import com.denizenscript.denizen.scripts.containers.core.ItemScriptHelper
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.world.items.ItemStack

class DenizenStackIdentifierParser(override val priority: Int = 0) : ItemStackIdentifier.Parser<DenizenStackIdentifier> {

    override fun from(stack: ItemStack): DenizenStackIdentifier? {
        val script = ItemScriptHelper.getItemScriptNameText(stack.unwrapSpigot()) ?: return null
        return DenizenStackIdentifier(stack, script)
    }
}