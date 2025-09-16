package com.wolfyscript.scafall.spigotlike.compat.magic

import com.elmakers.mine.bukkit.api.magic.MagicAPI
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import org.bukkit.Bukkit

class MagicStackIdentifierParser(override val priority: Int = 600) : ItemStackIdentifier.Parser<MagicStackIdentifier> {

    val magicAPI = Bukkit.getPluginManager().getPlugin("Magic") as? MagicAPI ?: error("Could not find Magic API!")

    override fun from(stack: ItemStackLike): MagicStackIdentifier? {
        val itemStack = stack.unwrapSpigot()
        if(magicAPI.isBrush(itemStack) || magicAPI.isSpell(itemStack) || magicAPI.isUpgrade(itemStack) || magicAPI.isWand(itemStack)) {
            return MagicStackIdentifier(magicAPI.getItemKey(itemStack));
        }
        return null
    }
}