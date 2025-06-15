package com.wolfyscript.scafall.wrappers.world.items

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.data.DataComponentMap
import com.wolfyscript.scafall.data.DataHolder
import com.wolfyscript.scafall.identifier.Key

/**
 * A mutable version of the ItemStack.
 * i.e. The [DataComponentMap] and possibly other properties can be modified directly and the same instance will reflect those changes.
 *
 * @see ItemStackSnapshot An immutable snapshot of the ItemStack
 */
interface ItemStack : DataHolder.Mutable<ItemStack>, ItemStackLike<ItemStack, DataComponentMap.Mutable<ItemStack>> {

    companion object {

        fun of(itemType: Key): ItemStack {
            return ScafallProvider.get().factories.itemsFactory.createStack(itemType)
        }

        fun of(mcItemType: String) : ItemStack {
            return of(Key.key(Key.MINECRAFT_NAMESPACE, mcItemType))
        }

    }

    /**
     * Creates a snapshot of the whole ItemStack
     *
     * @return The snapshot ItemStack of this ItemStack.
     */
    fun snapshot(): ItemStackSnapshot

}
