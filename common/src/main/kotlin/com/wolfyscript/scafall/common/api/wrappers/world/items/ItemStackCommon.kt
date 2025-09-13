package com.wolfyscript.scafall.common.api.wrappers.world.items

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.wrappers.unwrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStack
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import net.minecraft.SharedConstants

class ItemStackCommon @JsonCreator(mode = JsonCreator.Mode.DISABLED) private constructor(
    mcStack: net.minecraft.world.item.ItemStack,
) : ItemStackLikeCommon(mcStack), ItemStack {

    @get:JsonProperty("version")
    val version: Int
        get() = SharedConstants.getCurrentVersion().dataVersion().version

    companion object {

        fun fromVanilla(stack: net.minecraft.world.item.ItemStack): ItemStackCommon {
            return ItemStackCommon(stack)
        }

        /**
         * Used to parse an ItemStack from a single String value.
         * The version is assumed to be the current data version and bypasses the [com.mojang.datafixers.DataFixerUpper]
         *
         * This JsonCreator is used when this stack wrapper is defined as a simple String value in JSON.
         */
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        @JvmStatic
        fun createFromSNBT(snbt: String): ItemStackCommon {
            return ItemStackCommon(
                ScafallProvider.get().factories.itemsFactory.parseFromSNBT(
                    snbt,
                    SharedConstants.getCurrentVersion().dataVersion().version
                ).unwrap()
            )
        }

        /**
         * Used to parse the ItemStack from a SNBT string and version.
         * If the version indicates that an upgrade is necessary, then the parsed NBT is upgraded using the [com.mojang.datafixers.DataFixerUpper] before parsing the [net.minecraft.world.item.ItemStack]
         *
         * This JsonCreator is the counterpart to the default serialization of this stack wrapper, which includes SNBT and version.
         */
        @JsonCreator
        @JvmStatic
        fun createFromSNBT(
            @JsonProperty("snbt") snbt: String,
            @JsonProperty("version") version: Int,
        ): ItemStackCommon {
            return ItemStackCommon(ScafallProvider.get().factories.itemsFactory.parseFromSNBT(snbt, version).unwrap())
        }

    }

    override fun snapshot(): ItemStackSnapshot {
        return ItemStackSnapshotCommon(mcStack.copy())
    }

}