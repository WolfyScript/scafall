package com.wolfyscript.scafall.spigot.api.wrappers.world.items

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.common.api.data.SnapshotDataComponentMap
import com.wolfyscript.scafall.data.DataKey
import com.wolfyscript.scafall.eval.context.EvalContext
import com.wolfyscript.scafall.eval.value_provider.*
import com.wolfyscript.scafall.nbt.*
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackConfig
import de.tr7zw.nbtapi.NBTCompound
import de.tr7zw.nbtapi.NBTList
import de.tr7zw.nbtapi.NBTType
import de.tr7zw.nbtapi.iface.ReadableNBT
import de.tr7zw.nbtapi.iface.ReadableNBTList
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Material
import java.util.function.BiFunction

class BukkitItemStackConfig : ItemStackConfig {
    private val usePaperDisplayOptions: Boolean = false // TODO
    private val HANDLED_NBT_TAGS = setOf("display.Name", "display.Lore", "CustomModelData", "Damage", "Enchantments")

    @JsonCreator
    constructor(@JsonProperty("itemId") itemId: String) : super(
        itemId, SnapshotDataComponentMap()
    )

    constructor(wrappedStack: ItemStack) : super(
        (wrappedStack as BukkitItemStack).bukkitRef!!.type.key.toString(),
        SnapshotDataComponentMap()
    ) {
        val stack = wrappedStack.bukkitRef

        // Read from ItemStack
        this.amount = ValueProviderIntegerConst(stack!!.amount)

        for (dataKey in ScafallProvider.get().registries.itemDataKeyRegistry) {
            // We iterate over all the available dataKeys and check if it finds anything... this may be optimized
            dataKey.readFrom(wrappedStack)?.let {
                data().set(dataKey, it)
            }
        }

    }

    override fun constructItemStack(
        context: EvalContext,
        miniMessage: MiniMessage?,
        tagResolvers: TagResolver
    ): BukkitItemStack? {
        val type = Material.matchMaterial(itemId)
        if (type != null) {
            val itemStack = org.bukkit.inventory.ItemStack(type)
            itemStack.amount = amount.getValue(context)

            // Apply ItemMeta afterwards to override possible NBT Tags
            val wrappedStack = BukkitItemStack(itemStack)

            for (dataKey in data().keys()) {
                applyDataKey(wrappedStack, dataKey)
            }
            return wrappedStack
        }
        return null
    }

    private fun <T: Any> applyDataKey(stack: ItemStack, dataKey: DataKey<T, ItemStack>) {
        data().get(dataKey)?.let {
            dataKey.writeTo(it, stack)
        }
    }

    private fun readFromItemStack(
        currentCompound: ReadableNBT,
        path: String,
        parent: NBTTagConfig?
    ): NBTTagConfigCompound {
        val configCompound = NBTTagConfigCompound(parent)
        val children: MutableMap<String, NBTTagConfig> = HashMap()
        for (key in currentCompound.keys) {
            val childPath = if (path.isEmpty()) key else ("$path.$key")
            if (HANDLED_NBT_TAGS.contains(childPath)) {
                // Skip already handled NBT Tags, so they are not both in common and NBT settings!
                continue
            }
            val childConfig = when (currentCompound.getType(key)) {
                NBTType.NBTTagCompound -> {
                    val readConfigCompound =
                        currentCompound.getCompound(key)?.let { readFromItemStack(it, childPath, configCompound) }
                    readConfigCompound
                }

                NBTType.NBTTagList -> when (currentCompound.getListType(key)) {
                    NBTType.NBTTagCompound -> {
                        val compoundConfigList = NBTTagConfigListCompound(parent, listOf())
                        val compoundList = currentCompound.getCompoundList(key)
                        val elements: MutableList<NBTTagConfigCompound> = ArrayList()
                        for ((index, listCompound) in compoundList.withIndex()) {
                            elements.add(readFromItemStack(listCompound, "$childPath.$index", compoundConfigList))
                        }
                        compoundConfigList.values = elements
                        compoundConfigList
                    }

                    NBTType.NBTTagInt -> readPrimitiveList(
                        currentCompound.getIntegerList(key),
                        NBTTagConfigListInt(configCompound, ArrayList())
                    ) { listInt: NBTTagConfigListPrimitive<Int, NBTTagConfigInt>, integer: Int ->
                        NBTTagConfigInt(
                            listInt, ValueProviderIntegerConst(
                                integer
                            )
                        )
                    }

                    NBTType.NBTTagIntArray -> readPrimitiveList(
                        currentCompound.getIntArrayList(key),
                        NBTTagConfigListIntArray(configCompound, ArrayList())
                    ) { listIntArray: NBTTagConfigListPrimitive<IntArray, NBTTagConfigIntArray>, intArray ->
                        NBTTagConfigIntArray(
                            listIntArray,
                            ValueProviderIntArrayConst(intArray)
                        )
                    }

                    NBTType.NBTTagLong -> readPrimitiveList(
                        currentCompound.getLongList(key),
                        NBTTagConfigListLong(configCompound, ArrayList())
                    ) { listConfig: NBTTagConfigListPrimitive<Long, NBTTagConfigLong>, aLong: Long ->
                        NBTTagConfigLong(
                            listConfig, ValueProviderLongConst(
                                aLong
                            )
                        )
                    }

                    NBTType.NBTTagFloat -> readPrimitiveList(
                        currentCompound.getFloatList(key),
                        NBTTagConfigListFloat(configCompound, ArrayList())
                    ) { listConfig: NBTTagConfigListPrimitive<Float, NBTTagConfigFloat>, aFloat: Float ->
                        NBTTagConfigFloat(
                            listConfig, ValueProviderFloatConst(
                                aFloat
                            )
                        )
                    }

                    NBTType.NBTTagDouble -> readPrimitiveList(
                        currentCompound.getDoubleList(key),
                        NBTTagConfigListDouble(configCompound, ArrayList())
                    ) { listConfig: NBTTagConfigListPrimitive<Double, NBTTagConfigDouble>, aDouble: Double ->
                        NBTTagConfigDouble(
                            listConfig, ValueProviderDoubleConst(
                                aDouble
                            )
                        )
                    }

                    NBTType.NBTTagString -> readPrimitiveList(
                        currentCompound.getStringList(key),
                        NBTTagConfigListString(configCompound, ArrayList())
                    ) { listConfig: NBTTagConfigListPrimitive<String, NBTTagConfigString>, aString: String ->
                        NBTTagConfigString(
                            listConfig,
                            ValueProviderStringConst(aString)
                        )
                    }

                    else -> null
                }

                NBTType.NBTTagByte -> NBTTagConfigByte(
                    configCompound,
                    ValueProviderByteConst(currentCompound.getByte(key))
                )

                NBTType.NBTTagByteArray -> NBTTagConfigByteArray(
                    configCompound,
                    ValueProviderByteArrayConst(currentCompound.getByteArray(key) ?: ByteArray(0))
                )

                NBTType.NBTTagShort -> NBTTagConfigShort(
                    configCompound,
                    ValueProviderShortConst(currentCompound.getShort(key))
                )

                NBTType.NBTTagInt -> NBTTagConfigInt(
                    configCompound,
                    ValueProviderIntegerConst(currentCompound.getInteger(key))
                )

                NBTType.NBTTagIntArray -> NBTTagConfigIntArray(
                    configCompound,
                    ValueProviderIntArrayConst(currentCompound.getIntArray(key) ?: IntArray(0))
                )

                NBTType.NBTTagLong -> NBTTagConfigLong(
                    configCompound,
                    ValueProviderLongConst(currentCompound.getLong(key))
                )

                NBTType.NBTTagFloat -> NBTTagConfigFloat(
                    configCompound,
                    ValueProviderFloatConst(currentCompound.getFloat(key))
                )

                NBTType.NBTTagDouble -> NBTTagConfigDouble(
                    configCompound,
                    ValueProviderDoubleConst(currentCompound.getDouble(key))
                )

                NBTType.NBTTagString -> NBTTagConfigString(
                    configCompound,
                    ValueProviderStringConst(currentCompound.getString(key))
                )

                else -> null
            }
            if (childConfig != null) {
                children[key] = childConfig
            }
        }
        configCompound.children = children
        return configCompound
    }

    /**
     * Reads the elements of a NBTList and converts them, using the given function, to the NBTTagConfig.
     *
     * @param nbtList            The NBTList from the NBTItemAPI
     * @param configList         The instance of the NBTTagConfigList to load the elements into.
     * @param elementConstructor This constructs each element of list.
     * @param <T>                The primitive data type.
     * @param <VAL>              The type of the Element config.
     * @return The configList instance with the new elements.
    </VAL></T> */
    private fun <T, VAL : NBTTagConfigPrimitive<T>> readPrimitiveList(
        nbtList: ReadableNBTList<T>,
        configList: NBTTagConfigListPrimitive<T, VAL>,
        elementConstructor: BiFunction<NBTTagConfigListPrimitive<T, VAL>, T, VAL>
    ): NBTTagConfigListPrimitive<T, VAL> {
        configList.values =
            nbtList.toListCopy().stream().map { value: T -> elementConstructor.apply(configList, value) }
                .toList()
        return configList
    }

    private fun applyCompound(compound: NBTCompound, config: NBTTagConfigCompound, context: EvalContext) {
        for ((tagName, tagConfig) in config.children) {
            if (tagConfig is NBTTagConfigCompound) {
                applyCompound(compound.addCompound(tagName), tagConfig, context)
            } else if (tagConfig is NBTTagConfigList<*>) {
                applyList(compound, tagName, tagConfig, context)
            } else if (tagConfig is NBTTagConfigByte) {
                compound.setByte(tagName, tagConfig.value.getValue(context))
            } else if (tagConfig is NBTTagConfigByteArray) {
                compound.setByteArray(tagName, tagConfig.value.getValue(context))
            } else if (tagConfig is NBTTagConfigShort) {
                compound.setShort(tagName, tagConfig.value.getValue(context))
            } else if (tagConfig is NBTTagConfigInt) {
                compound.setInteger(tagName, tagConfig.value.getValue(context))
            } else if (tagConfig is NBTTagConfigIntArray) {
                compound.setIntArray(tagName, tagConfig.value.getValue(context))
            } else if (tagConfig is NBTTagConfigLong) {
                compound.setLong(tagName, tagConfig.value.getValue(context))
            } else if (tagConfig is NBTTagConfigFloat) {
                compound.setFloat(tagName, tagConfig.value.getValue(context))
            } else if (tagConfig is NBTTagConfigDouble) {
                compound.setDouble(tagName, tagConfig.value.getValue(context))
            } else if (tagConfig is NBTTagConfigString) {
                compound.setString(tagName, tagConfig.value.getValue(context))
            } else if (tagConfig is NBTTagConfigBoolean) {
                compound.setBoolean(tagName, tagConfig.getValue(context))
            }
        }
    }

    private fun applyList(
        compound: NBTCompound,
        tagName: String,
        configList: NBTTagConfigList<*>,
        context: EvalContext
    ) {
        if (configList is NBTTagConfigListCompound) {
            val list = compound.getCompoundList(tagName)
            for (element in configList.elements) {
                applyCompound(list.addCompound(), element.value, context)
            }
        } else if (configList is NBTTagConfigListInt) {
            applyPrimitiveList(compound.getIntegerList(tagName), configList, context)
        } else if (configList is NBTTagConfigListLong) {
            applyPrimitiveList(compound.getLongList(tagName), configList, context)
        } else if (configList is NBTTagConfigListFloat) {
            applyPrimitiveList(compound.getFloatList(tagName), configList, context)
        } else if (configList is NBTTagConfigListDouble) {
            applyPrimitiveList(compound.getDoubleList(tagName), configList, context)
        } else if (configList is NBTTagConfigListString) {
            applyPrimitiveList(compound.getStringList(tagName), configList, context)
        } else if (configList is NBTTagConfigListIntArray) {
            applyPrimitiveList(compound.getIntArrayList(tagName), configList, context)
        }
    }

    private fun <T> applyPrimitiveList(
        nbtList: NBTList<T>,
        configPrimitive: NBTTagConfigList<out NBTTagConfigPrimitive<T>>,
        context: EvalContext
    ) {
        for (element in configPrimitive.elements) {
            nbtList.add(element.value.value.getValue(context)) // This looks weird, but it will provide more options in the future.
        }
    }

    override fun toString(): String {
        return "BukkitItemStackConfig{" +
                "itemId='" + itemId + '\'' +
                "} "
    }
}
