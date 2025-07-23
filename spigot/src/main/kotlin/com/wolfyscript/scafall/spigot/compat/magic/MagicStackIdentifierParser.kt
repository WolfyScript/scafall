package com.wolfyscript.scafall.spigot.compat.magic

import com.elmakers.mine.bukkit.api.magic.MagicAPI
import com.fasterxml.jackson.annotation.JsonIgnore
import com.wolfyscript.scafall.items.ItemStackIdentifier
import com.wolfyscript.scafall.spigot.api.wrappers.utils.unwrapSpigot
import com.wolfyscript.scafall.spigot.compat.mmoitems.MMOItemsStackIdentifier
import com.wolfyscript.scafall.spigot.compat.mythicmobs.MythicMobsStackIdentifier
import com.wolfyscript.scafall.spigot.compat.oraxen.OraxenItemStackIdentifier
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import io.lumine.mythic.bukkit.MythicBukkit
import io.th0rgal.oraxen.api.OraxenItems
import net.Indyuce.mmoitems.MMOItems
import org.bukkit.Bukkit

class MagicStackIdentifierParser(override val priority: Int = 600) : ItemStackIdentifier.Parser<MagicStackIdentifier> {

    val magicAPI = Bukkit.getPluginManager().getPlugin("Magic") as? MagicAPI ?: error("Could not find Magic API!")

    override fun from(stack: ItemStack): MagicStackIdentifier? {
        val itemStack = stack.unwrapSpigot()
        if(magicAPI.isBrush(itemStack) || magicAPI.isSpell(itemStack) || magicAPI.isUpgrade(itemStack) || magicAPI.isWand(itemStack)) {
            return MagicStackIdentifier(magicAPI.getItemKey(itemStack));
        }
        return null
    }
}