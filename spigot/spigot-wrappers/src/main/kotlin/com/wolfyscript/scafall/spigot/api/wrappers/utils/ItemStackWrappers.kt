package com.wolfyscript.scafall.spigot.api.wrappers.utils

import com.wolfyscript.scafall.ScafallProvider
import com.wolfyscript.scafall.wrappers.minecraft.snapshot
import com.wolfyscript.scafall.wrappers.minecraft.unwrap
import com.wolfyscript.scafall.wrappers.minecraft.wrap
import com.wolfyscript.scafall.wrappers.world.items.ItemStackLike
import com.wolfyscript.scafall.wrappers.world.items.ItemStackSnapshot
import com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Field

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

/**
 * Wraps the Minecraft Stack of this Bukkit ItemStack in a scafall [ItemStack][com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack].
 *
 * #### **Warning!**
 *
 * Bukkit ItemStacks **may not have Minecraft ItemStack** associated with them! (e.g. when created via the [ItemStack] constructor)
 *
 * In those cases, the wrapped stack is **not linked to the original Bukkit stack**,
 * and **changes to the wrapped stack won't be reflected on the Bukkit stack!**
 *
 * #### Alternative
 * If a consistent behaviour is required use [snapshot] instead!
 *
 * @see snapshot
 */
fun ItemStack.wrap() : com.wolfyscript.scafall.wrappers.world.items.ScafallItemStack {
    // Note ItemStacks may not be CraftItemStacks (e.g. created via ItemStack constructor).
    // In that case, we simply create the NMS stack copy of it. However, changes to the wrapped stack won't apply to the original!
    val craftStack = this as? CraftItemStack ?: return CraftItemStack.asNMSCopy(this).wrap()
    // When it is a CraftItemStack, we need to use a little reflection to access the handle.
    if (craftStackHandleField != null) {
        return (craftStackHandleField.get(craftStack) as net.minecraft.world.item.ItemStack).wrap()
    }
    // or fallback to a copy if field is not available for whatever reason
    return CraftItemStack.asNMSCopy(craftStack).wrap()
}

/**
 * Wraps the Minecraft Stack of this Bukkit ItemStack in a scafall [ItemStackSnapshot].
 *
 * _The warning of [ItemStack.wrap] does not apply here, because [ItemStackSnapshots][ItemStackSnapshot] are immutable (changes are never reflected on the original)_
 */
fun ItemStack.snapshot() : ItemStackSnapshot {
    return CraftItemStack.asNMSCopy(this).snapshot()
}

/**
 * Unwraps the Scafall [ItemStackSnapshot] to a Bukkit [ItemStack].
 *
 * @return The Bukkit ItemStack representation of the Scafall ItemStackSnapshot.
 */
fun ItemStackSnapshot.unwrapSpigot(): ItemStack {
    return CraftItemStack.asCraftMirror(this.unwrap())
}

/**
 * Unwraps the Scafall [ItemStack] to a Bukkit [ItemStack].
 *
 * @return The Bukkit ItemStack representation of the Scafall ItemStack.
 */
fun ScafallItemStack.unwrapSpigot(): ItemStack {
    return CraftItemStack.asCraftMirror(this.unwrap())
}

/**
 * Unwraps this item stack wrapper to a Bukkit [ItemStack]
 */
fun ItemStackLike.unwrapSpigot(): ItemStack {
    return CraftItemStack.asCraftMirror(this.unwrap())
}
