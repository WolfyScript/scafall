package com.wolfyscript.scaffolding.spigot.api.wrappers.world.items

import com.wolfyscript.scaffolding.identifier.Key
import com.wolfyscript.scaffolding.spigot.api.data.ItemStackDataComponentMap
import com.wolfyscript.scaffolding.spigot.api.identifiers.api
import com.wolfyscript.scaffolding.spigot.api.wrappers.BukkitRefAdapter
import com.wolfyscript.scaffolding.wrappers.world.items.ItemStackConfig
import org.bukkit.inventory.ItemStack

class ItemStackImpl(bukkitRef: ItemStack?) : BukkitRefAdapter<ItemStack?>(bukkitRef), com.wolfyscript.scaffolding.wrappers.world.items.ItemStack {
    private val componentMap = ItemStackDataComponentMap(this)

    override val item: Key
        get() {
            if (bukkitRef == null) {
                return Key.parse("minecraft:air")
            }
            return bukkitRef.type.key.api()
        }

    override val amount: Int
        get() =  bukkitRef?.amount ?: 0

    override fun snapshot(): ItemStackConfig {
        return BukkitItemStackConfig(this)
    }

    override fun data(): ItemStackDataComponentMap {
        return componentMap
    }
}
