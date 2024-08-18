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
package com.wolfyscript.scaffolding.spigot.platform.world.items

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.wolfyscript.scaffolding.ScaffoldingProvider
import de.tr7zw.changeme.nbtapi.NBTCompound
import de.tr7zw.changeme.nbtapi.NBTItem
import net.kyori.adventure.platform.bukkit.BukkitComponentSerializer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.lang.reflect.Field
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import java.util.regex.Pattern

@JsonAutoDetect(
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
abstract class AbstractItemBuilder<Self: AbstractItemBuilder<Self>> protected constructor(@field:JsonIgnore private val typeClass: Class<Self>) {
    @JsonIgnore
    private val miniMessage =
        ScaffoldingProvider.get().adventure.miniMsg

    abstract val itemStack: ItemStack

    abstract fun create(): ItemStack?

    fun get(): Self {
        return typeClass.cast(this)
    }

    /**
     * @param itemMeta The ItemMeta to add to the ItemStack.
     * @return This [AbstractItemBuilder] instance. Used for chaining of methods.
     */
    fun setItemMeta(itemMeta: ItemMeta?): Self {
        itemStack.setItemMeta(itemMeta)
        return get()
    }

    @get:JsonIgnore
    val itemMeta: ItemMeta
        get() = itemStack.itemMeta

    fun hasItemMeta(): Boolean {
        return itemStack.hasItemMeta()
    }

    fun addEnchantment(ench: Enchantment, level: Int): Self {
        itemStack.addEnchantment(ench, level)
        return get()
    }

    fun removeEnchantment(ench: Enchantment): Self {
        itemStack.removeEnchantment(ench)
        return get()
    }

    fun addUnsafeEnchantment(ench: Enchantment, level: Int): Self {
        itemStack.addUnsafeEnchantment(ench, level)
        return get()
    }

    fun addEnchantments(enchantments: Map<Enchantment?, Int?>): Self {
        itemStack.addEnchantments(enchantments)
        return get()
    }

    fun addUnsafeEnchantments(enchantments: Map<Enchantment?, Int?>): Self {
        itemStack.addUnsafeEnchantments(enchantments)
        return get()
    }

    fun addItemFlags(vararg itemFlags: ItemFlag): Self {
        val itemMeta = itemStack.itemMeta
        itemMeta.addItemFlags(*itemFlags)
        return setItemMeta(itemMeta)
    }

    fun removeItemFlags(vararg itemFlags: ItemFlag): Self {
        val itemMeta = itemStack.itemMeta
        itemMeta.removeItemFlags(*itemFlags)
        return setItemMeta(itemMeta)
    }

    fun setDisplayName(name: String?): Self {
        val itemMeta = itemStack.itemMeta
        itemMeta.setDisplayName(name)
        return setItemMeta(itemMeta)
    }

    fun setType(type: Material): Self {
        itemStack.type = type
        return get()
    }

    fun setLore(lore: List<String>?): Self {
        val itemMeta = itemStack.itemMeta
        itemMeta.lore = lore
        return setItemMeta(itemMeta)
    }

    fun addLoreLine(line: String): Self {
        val itemMeta = itemStack.itemMeta
        val lore = if (itemMeta.hasLore()) itemMeta.lore else ArrayList()
        lore!!.add(line)
        return setLore(lore)
    }

    fun addLoreLine(index: Int, line: String): Self {
        val itemMeta = itemStack.itemMeta
        val lore = if (itemMeta.hasLore()) itemMeta.lore else ArrayList()
        lore!!.add(index, line)
        return setLore(lore)
    }

    /**
     * Checks if this item has Custom Durability set.
     */
    fun hasCustomDurability(): Boolean {
        val itemMeta = itemMeta
        if (itemMeta != null) {
            return itemMeta.persistentDataContainer.has(CUSTOM_DURABILITY_VALUE, PersistentDataType.INTEGER)
        }
        return false
    }

    fun setCustomDamage(damage: Int): Self {
        val itemMeta = itemMeta
        if (itemMeta != null) {
            val dataContainer = itemMeta.persistentDataContainer
            dataContainer.set(CUSTOM_DURABILITY_DAMAGE, PersistentDataType.INTEGER, damage)
            updateCustomDurabilityTag(itemMeta)
        }
        if (itemMeta is Damageable) {
            var value =
                (itemStack.type.maxDurability * (damage.toDouble() / getCustomDurability(itemMeta).toDouble())).toInt()
            if (damage > 0 && value <= 0) {
                value = damage
            }
            itemMeta.damage = value
        }
        return setItemMeta(itemMeta)
    }

    val customDamage: Int
        get() = getCustomDamage(itemMeta)

    fun getCustomDamage(itemMeta: ItemMeta?): Int {
        if (itemMeta != null) {
            return itemMeta.persistentDataContainer.getOrDefault(
                CUSTOM_DURABILITY_DAMAGE,
                PersistentDataType.INTEGER,
                0
            )
        }
        return 0
    }

    fun setCustomDurability(durability: Int): Self {
        val itemMeta = itemMeta
        if (itemMeta != null) {
            val dataContainer = itemMeta.persistentDataContainer
            dataContainer.set(CUSTOM_DURABILITY_VALUE, PersistentDataType.INTEGER, durability)
            if (!dataContainer.has(CUSTOM_DURABILITY_DAMAGE, PersistentDataType.INTEGER)) {
                dataContainer.set(CUSTOM_DURABILITY_DAMAGE, PersistentDataType.INTEGER, 0)
            }
            updateCustomDurabilityTag()
        }
        return setItemMeta(itemMeta)
    }

    val customDurability: Int
        get() = getCustomDurability(itemMeta)

    fun getCustomDurability(itemMeta: ItemMeta?): Int {
        if (itemMeta != null) {
            return itemMeta.persistentDataContainer.getOrDefault(CUSTOM_DURABILITY_VALUE, PersistentDataType.INTEGER, 0)
        }
        return 0
    }

    fun removeCustomDurability(): Self {
        val itemMeta = itemMeta
        if (itemMeta != null) {
            val dataContainer = itemMeta.persistentDataContainer
            dataContainer.remove(CUSTOM_DURABILITY_VALUE)
            dataContainer.remove(CUSTOM_DURABILITY_DAMAGE)
            dataContainer.remove(CUSTOM_DURABILITY_TAG)
        }
        return setItemMeta(itemMeta)
    }

    fun setCustomDurabilityTag(tag: String): Self {
        val itemMeta = itemMeta
        if (itemMeta != null) {
            val dataContainer = itemMeta.persistentDataContainer
            val tagContainer = dataContainer.adapterContext.newPersistentDataContainer()
            tagContainer.set(CUSTOM_DURABILITY_TAG_CONTENT, PersistentDataType.STRING, tag)
            tagContainer.set(CUSTOM_DURABILITY_TAG_MINIMSG, PersistentDataType.BYTE, 1.toByte())
            dataContainer.set(CUSTOM_DURABILITY_TAG, PersistentDataType.TAG_CONTAINER, tagContainer)
            updateCustomDurabilityTag()
        }
        return setItemMeta(itemMeta)
    }

    val customDurabilityTag: String
        /**
         * Returns the raw tag content that is specified for this ItemMeta.<br></br>
         *
         * @return The raw content (text) of the durability tag.
         */
        get() = getCustomDurabilityTag(itemMeta)

    /**
     * Returns the raw tag content that is specified for this ItemMeta.<br></br>
     *
     * @param itemMeta The ItemMeta, from which to get the tag.
     * @return The raw content (text) of the durability tag.
     */
    fun getCustomDurabilityTag(itemMeta: ItemMeta?): String {
        if (itemMeta != null) {
            val dataContainer = itemMeta.persistentDataContainer
            var miniMsg = false
            var content = ""
            if (dataContainer.has<PersistentDataContainer, PersistentDataContainer>(
                    CUSTOM_DURABILITY_TAG,
                    PersistentDataType.TAG_CONTAINER
                )
            ) {
                val tagContainer = dataContainer.get(CUSTOM_DURABILITY_TAG, PersistentDataType.TAG_CONTAINER)
                miniMsg =
                    tagContainer!!.getOrDefault(CUSTOM_DURABILITY_TAG_MINIMSG, PersistentDataType.BYTE, 1.toByte())
                        .toInt() == 1
                content = tagContainer.getOrDefault(CUSTOM_DURABILITY_TAG_CONTENT, PersistentDataType.STRING, "")
            } else if (dataContainer.has<String, String>(CUSTOM_DURABILITY_TAG, PersistentDataType.STRING)) {
                //Using old tag version
                content = dataContainer.getOrDefault(CUSTOM_DURABILITY_TAG, PersistentDataType.STRING, "")
                    .replace("%dur%", "<dur>").replace("%max_dur%", "<max_dur>")
            }
            return if (miniMsg) content else miniMessage.serialize(
                BukkitComponentSerializer.legacy().deserialize(content)
            )
        }
        return ""
    }

    val customDurabilityTagComponent: Component
        get() = getCustomDurabilityTagComponent(itemMeta)

    fun getCustomDurabilityTagComponent(itemMeta: ItemMeta?): Component {
        return miniMessage.deserialize(
            getCustomDurabilityTag(itemMeta),
            Placeholder.parsed("dur", (getCustomDurability(itemMeta) - getCustomDamage(itemMeta)).toString()),
            Placeholder.parsed("max_dur", getCustomDurability(itemMeta).toString())
        )
    }

    fun updateCustomDurabilityTag(): Self {
        val itemMeta = itemMeta
        updateCustomDurabilityTag(itemMeta)
        return setItemMeta(itemMeta)
    }

    fun updateCustomDurabilityTag(itemMeta: ItemMeta?) {
        if (itemMeta != null) {
            val tag = BukkitComponentSerializer.legacy().serialize(getCustomDurabilityTagComponent(itemMeta))
            val dataContainer = itemMeta.persistentDataContainer
            val lore = if (itemMeta.lore != null) itemMeta.lore else ArrayList()
            if (dataContainer.has<Int, Int>(CUSTOM_DURABILITY_INDEX, PersistentDataType.INTEGER)) {
                val index = dataContainer.get(CUSTOM_DURABILITY_INDEX, PersistentDataType.INTEGER)!!
                if (index < lore!!.size) {
                    lore[index] = tag
                    itemMeta.lore = lore
                    return
                }
            } else {
                dataContainer.set(CUSTOM_DURABILITY_INDEX, PersistentDataType.INTEGER, lore!!.size)
            }
            lore.add(tag)
            itemMeta.lore = lore
        }
    }

    fun removePlayerHeadValue(): Self {
        if (itemMeta is SkullMeta) {
            //GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            //profile.getProperties().put("textures", new Property("textures", null));
            var profileField: Field? = null
            try {
                profileField = itemMeta.javaClass.getDeclaredField("profile")
                profileField.isAccessible = true
                profileField[itemMeta] = null
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: SecurityException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            return setItemMeta(itemMeta)
        }
        return get()
    }

    fun setPlayerHeadURL(value: String): Self {
        if (value.startsWith("http://textures.minecraft.net/texture/")) {
            return setPlayerHeadValue(value)
        }
        return setPlayerHeadValue("http://textures.minecraft.net/texture/$value")
    }

    fun setPlayerHeadURL(value: String, name: String?, uuid: UUID?): Self {
        if (value.startsWith("http://textures.minecraft.net/texture/")) {
            return setPlayerHeadValue(value, name, uuid)
        }
        return setPlayerHeadValue("http://textures.minecraft.net/texture/$value", name, uuid)
    }

    val playerHeadValue: String
        get() {
            if (itemMeta is SkullMeta) {
                val nbtItem = NBTItem(itemStack)
                val skull = nbtItem.getCompound("SkullOwner")
                if (skull != null) {
                    if (skull.hasKey("Properties")) {
                        val properties = skull.getCompound("Properties")
                        if (properties.hasKey("textures")) {
                            val textures = properties.getCompoundList("textures")
                            if (textures.size > 0) {
                                val `object`: NBTCompound = textures[0]
                                val value = `object`.getString("Value")
                                return value ?: ""
                            }
                        }
                    }
                }
            }
            return ""
        }

    fun setPlayerHeadValue(value: String, name: String?, uuid: UUID?): Self {
        if (itemMeta is SkullMeta) {
            val profile = Bukkit.createPlayerProfile(uuid, name)

            var textureUrl = value
            if (!value.startsWith("https://") && !value.startsWith("http://")) {
                //WolfyUtilCore.getInstance().getLogger().log(Level.WARNING, String.format("Using Base64 Texture property (%s) to create player head: %s", value, getItemStack()));
                // The value is a base64 encoded texture value. This was previously supported, but requires a conversion now.
                try {
                    val decoded = String(Base64.getDecoder().decode(value))
                    val matcher = SKIN_TEXTURE_PATTERN.matcher(decoded)
                    if (!matcher.find()) {
                        ScaffoldingProvider.get().logger.error(
                            "Could not apply player head texture \"{}\" to stack {}: Failed to match decoded value {}",
                            value,
                            itemStack,
                            decoded
                        )
                        return get()
                    }
                    try {
                        textureUrl = matcher.group(1)
                    } catch (e: IllegalStateException) {
                        ScaffoldingProvider.get().logger.error(
                            String.format(
                                "Could not apply player head texture \"%s\" to stack %s: Failed to match decoded value %s",
                                value,
                                itemStack,
                                decoded
                            ), e
                        )
                        return get()
                    } catch (e: IndexOutOfBoundsException) {
                        ScaffoldingProvider.get().logger.error(
                            String.format(
                                "Could not apply player head texture \"%s\" to stack %s: Failed to match decoded value %s",
                                value,
                                itemStack,
                                decoded
                            ), e
                        )
                        return get()
                    }
                } catch (e: IllegalArgumentException) {
                    ScaffoldingProvider.get().logger.error(
                        String.format(
                            "Could not apply player head texture \"%s\" to stack %s: Value is neither an URL nor a base64 encoded texture value!",
                            value,
                            itemStack
                        ), e
                    )
                    return get()
                }
            }

            try {
                profile.textures.skin = URL(textureUrl)
            } catch (e: MalformedURLException) {
                ScaffoldingProvider.get().logger.warn(
                    String.format(
                        "Could not apply player head texture \"%s\" to stack %s", value,
                        itemStack
                    ), e
                )
                return get()
            }

            (itemMeta as SkullMeta).setOwnerProfile(profile)
            setItemMeta(itemMeta)
        }
        return get()
    }

    fun setPlayerHeadValue(value: String): Self {
        return setPlayerHeadValue(value, "none", UUID.randomUUID())
    }


    companion object {
        private val SKIN_TEXTURE_PATTERN: Pattern = Pattern.compile("\"SKIN\"\\s*:\\s*\\{\\s*\"url\"\\s*:\\s*\"(.*)\"")

        private val CUSTOM_DURABILITY_VALUE = NamespacedKey("wolfyutilities", "custom_durability.value")
        private val CUSTOM_DURABILITY_DAMAGE = NamespacedKey("wolfyutilities", "custom_durability.damage")
        private val CUSTOM_DURABILITY_INDEX = NamespacedKey("wolfyutilities", "custom_durability.index")
        private val CUSTOM_DURABILITY_TAG = NamespacedKey("wolfyutilities", "custom_durability.tag")
        private val CUSTOM_DURABILITY_TAG_CONTENT = NamespacedKey("wolfyutilities", "content")
        private val CUSTOM_DURABILITY_TAG_MINIMSG = NamespacedKey("wolfyutilities", "mini_msg")
    }
}
