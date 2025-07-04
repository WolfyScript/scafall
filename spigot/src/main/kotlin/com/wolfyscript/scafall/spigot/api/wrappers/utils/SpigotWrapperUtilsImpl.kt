package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.common.api.wrappers.utils.CommonWrapperUtilsImpl
import com.wolfyscript.scafall.common.api.wrappers.world.items.ItemStackCommon
import com.wolfyscript.scafall.common.api.wrappers.world.items.ItemStackLikeCommon
import com.wolfyscript.scafall.common.api.wrappers.world.items.ItemStackSnapshotCommon
import com.wolfyscript.scafall.wrappers.utils.snapshot
import com.wolfyscript.scafall.wrappers.utils.wrap
import com.wolfyscript.scafall.wrappers.world.ScafallBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalBlockPos
import com.wolfyscript.scafall.wrappers.world.ScafallGlobalPrecisePos
import com.wolfyscript.scafall.wrappers.world.ScafallPrecisePos
import com.wolfyscript.scafall.wrappers.world.entity.Player
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import net.minecraft.world.phys.Vec3
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.craftbukkit.util.CraftLocation
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Field

class SpigotWrapperUtilsImpl : CommonWrapperUtilsImpl(), SpigotWrapperUtils {

    /**
     * A little reflection is necessary to get direct access to the handle of the CraftItemStack
     */
    private val craftStackHandleField: Field? = try {
        val field = CraftItemStack::class.java.getDeclaredField("handle")
        field.isAccessible = true
        field
    } catch (e: ReflectiveOperationException) {
        ScafallProvider.get().logger.error(
            "Failed to get the handle field from CraftItemStack! Please report this issue to the Scafall GitHub page!",
            e
        )
        null
    }

    //
    // ItemStacks
    //

    override fun wrapItemStack(spigotStack: ItemStack): com.wolfyscript.scafall.wrappers.world.items.ItemStack {
        // Note ItemStacks may not be CraftItemStacks (e.g. created via ItemStack constructor).
        // In that case, we simply create the NMS stack copy of it. However, changes to the wrapped stack won't apply to the original!
        val craftStack = spigotStack as? CraftItemStack ?: return CraftItemStack.asNMSCopy(spigotStack).wrap()
        // When it is a CraftItemStack, we need to use a little reflection to access the handle.
        if (craftStackHandleField != null) {
            return (craftStackHandleField.get(craftStack) as net.minecraft.world.item.ItemStack).wrap()
        }
        // or fallback to a copy if field is not available for whatever reason
        return CraftItemStack.asNMSCopy(craftStack).wrap()
    }

    override fun wrapItemStackSnapshot(spigotStack: ItemStack): ItemStackSnapshot {
        return CraftItemStack.asNMSCopy(spigotStack).snapshot()
    }

    override fun unwrapItemStack(wrappedStack: ItemStackLike<*, *>): ItemStack {
        if (wrappedStack !is ItemStackLikeCommon<*, *>) {
            throw IllegalArgumentException("Wrapped stack is not an instance of ${ItemStackLikeCommon::class.simpleName}")
        }

        return when (wrappedStack) {
            is ItemStackCommon -> {
                CraftItemStack.asCraftMirror(wrappedStack.mcStack)
            }

            is ItemStackSnapshotCommon -> {
                CraftItemStack.asBukkitCopy(wrappedStack.mcStack)
            }
        }
    }

    //
    // Position
    //

    override fun toPreciseGlobal(location: Location): ScafallGlobalPrecisePos? {
        if (location.world == null) {
            return null
        }
        return Vec3(location.x, location.y, location.z).wrap(location.world.key.toScafall())
    }

    override fun toPrecise(location: Location): ScafallPrecisePos {
        return Vec3(location.x, location.y, location.z).wrap()
    }

    override fun toBlockPos(location: Location): ScafallBlockPos {
        return CraftLocation.toBlockPosition(location).wrap()
    }

    override fun toBlockPosGlobal(location: Location): ScafallGlobalBlockPos? {
        if (location.world == null) {
            return null
        }
        return CraftLocation.toBlockPosition(location).wrap(location.world.key.toScafall())
    }

    //
    // Player
    //

    override fun wrapPlayer(player: org.bukkit.entity.Player): Player {
        return (player as CraftPlayer).handle.wrap()
    }

    override fun unwrapToSpigot(player: Player): org.bukkit.entity.Player? {
        return Bukkit.getPlayer(player.uuid)
    }

}