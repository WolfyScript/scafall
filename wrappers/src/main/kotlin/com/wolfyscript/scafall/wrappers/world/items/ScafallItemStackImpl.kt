package com.wolfyscript.scafall.wrappers.world.items

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import net.minecraft.SharedConstants
import net.minecraft.world.item.ItemStack

internal class ScafallItemStackImpl : ItemStackLikeCommon, ScafallItemStack {

    companion object {

        fun wrap(stack: ItemStack): ScafallItemStackImpl {
            return ScafallItemStackImpl(stack)
        }

    }

    @JsonIgnore
    private constructor(
        mcStack: ItemStack,
    ) : super(mcStack)

    /**
     * Used to parse an ItemStack from a single String value.
     * The version is assumed to be the current data version and bypasses the [com.mojang.datafixers.DataFixerUpper]
     *
     * This JsonCreator is used when this stack wrapper is defined as a simple String value in JSON.
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    constructor(snbt: String) : this(parseFromSNBT(
        snbt,
        SharedConstants.getCurrentVersion().dataVersion().version
    ).unwrap())

    /**
     * Used to parse the ItemStack from a SNBT string and version.
     * If the version indicates that an upgrade is necessary, then the parsed NBT is upgraded using the [com.mojang.datafixers.DataFixerUpper] before parsing the [ItemStack]
     *
     * This JsonCreator is the counterpart to the default serialization of this stack wrapper, which includes SNBT and version.
     */
    @JsonCreator
    internal constructor(
        @JsonProperty("snbt") snbt: String,
        @JsonProperty("version") version: Int,
    ) : this(parseFromSNBT(snbt, version).unwrap())

    @get:JsonProperty("version")
    val version: Int
        get() = SharedConstants.getCurrentVersion().dataVersion().version

    override fun snapshot(): ItemStackSnapshot {
        return ItemStackSnapshotImpl.wrap(mcStack)
    }

    override fun unwrap(): ItemStack {
        return mcStack
    }

}