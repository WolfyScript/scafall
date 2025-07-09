package com.wolfyscript.scafall.identifier

import com.wolfyscript.scafall.ScafallProvider
import net.kyori.adventure.key.Key
import net.minecraft.resources.ResourceLocation

/**
 * Converts this key to a scafall [com.wolfyscript.scafall.identifier.Key]
 */
fun Key.toScafall() : com.wolfyscript.scafall.identifier.Key = ScafallProvider.get().factories.identifierFactory.key(this.namespace(), this.value())

fun ResourceLocation.toScafall() : com.wolfyscript.scafall.identifier.Key = ScafallProvider.get().factories.identifierFactory.key(this.namespace, this.path)