/*
 *       WolfyUtilities, APIs and Utilities for Minecraft Spigot plugins
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.wolfyscript.scafall.spigot.platform.registry

import com.wolfyscript.scafall.identifier.Key
import com.wolfyscript.scafall.identifier.Key.Companion.parse
import com.wolfyscript.scafall.identifier.Key.Companion.defaultKey
import com.wolfyscript.scafall.registry.Registries
import com.wolfyscript.scafall.registry.RegistrySimple
import com.wolfyscript.scafall.spigot.platform.world.items.CustomItem
import com.wolfyscript.scafall.spigot.platform.world.items.reference.CustomItemStackIdentifier
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*
import java.util.stream.Collectors

class RegistryCustomItem internal constructor(registries: Registries) :
    RegistrySimple<CustomItem>(defaultKey("custom_items"), registries) {

    val namespaces: List<String>
        get() = map.keys.stream().map(Key::namespace).distinct().collect(Collectors.toList())

    /**
     * Get all the items of the specific namespace.
     *
     * @param namespace the namespace you want to get the items from
     * @return A list of all the items of the specific namespace
     */
    fun get(namespace: String): List<CustomItem> {
        return map.entries.stream().filter { entry -> entry.key.namespace == namespace }.map<CustomItem> { it.value }
            .collect(Collectors.toList())
    }

    /**
     * Gets a CustomItem of the specific ItemStack.
     * This method returns the original CustomItem from the ItemStack.
     * This only works if the itemStack contains a namespaced key corresponding to a CustomItem in this registry!
     *
     * If you need access to the original CustomItem variables use this method.
     *
     * @param itemStack The ItemStack to get the CustomItem from.
     * @return CustomItem the ItemStack is linked to, only if it is saved, else returns null
     */
    fun getByItemStack(itemStack: ItemStack?): CustomItem? {
        return getKeyOfItemMeta(itemStack?.itemMeta)?.let { this[it] }
    }

    /**
     * @param itemMeta The ItemMeta to get the key from.
     * @return The CustomItems [Key] from the ItemMeta; or null if the ItemMeta doesn't contain a key.
     */
    private fun getKeyOfItemMeta(itemMeta: ItemMeta?): Key? {
        return if (itemMeta == null) {
            null
        } else {
            parse(itemMeta.persistentDataContainer.get(CustomItem.PERSISTENT_KEY_TAG, PersistentDataType.STRING)!!)
        }
    }

    /**
     * @param namespacedKey NamespacedKey of the item
     * @return true if there is an CustomItem for the NamespacedKey
     */
    fun has(namespacedKey: Key): Boolean {
        return map.containsKey(namespacedKey)
    }

    /**
     * Removes the CustomItem from the registry.
     * However, this won't delete the config if one exists!
     * If a config exists the item will be reloaded on the next restart.
     *
     * @param namespacedKey The NamespacedKey of the CustomItem
     */
    fun remove(namespacedKey: Key?) {
        map.remove(namespacedKey)
    }

    /**
     * Add a CustomItem to the registry or update a existing one and sets the NamespacedKey in the CustomItem object.
     * <br></br>
     * If the registry already contains a value for the NamespacedKey then the value will be updated with the new one.
     * <br></br>
     * **
     * If the CustomItem is linked with a [WolfyUtilitiesRef], which NamespacedKey is the same as the passed in NamespacedKey, the CustomItem will neither be added or updated!
     * <br></br>
     * This is to prevent a infinite loop where a reference tries to call itself when it tries to get the values from it's parent item.
     ** *
     *
     * @param key The NamespacedKey the CustomItem will be saved under.
     * @param value          The CustomItem to add or update.
     */
    override fun register(key: Key, value: CustomItem) {
        val identifier = value.stackReference().identifier().get()
        if (identifier is CustomItemStackIdentifier && identifier.key() == key) {
            return
        }
        map[key] = value
    }
}
