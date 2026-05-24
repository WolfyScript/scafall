package com.wolfyscript.scafall.spigotlike.compat.denizen

import com.denizenscript.denizen.scripts.containers.core.ItemScriptHelper
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.minecraft.unwrap
import com.wolfyscript.scafall.wrappers.minecraft.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike

class DenizenStackIdentifierParser(override val priority: Int = 0) : ItemStackIdentifier.Parser<DenizenStackIdentifier> {

    override fun from(stack: ItemStackLike): DenizenStackIdentifier? {
        val script = ItemScriptHelper.getItemScriptNameText(stack.unwrapSpigot()) ?: return null
        return DenizenStackIdentifier(stack.unwrap().wrap(), script)
    }
}