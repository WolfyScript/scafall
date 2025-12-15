package com.wolfyscript.scafall.identifier

import com.wolfyscript.scafall.ScafallProvider
import net.kyori.adventure.key.Key
import net.minecraft.resources.Identifier

/**
 * Converts an Adventure key to a scafall [com.wolfyscript.scafall.identifier.Key]
 */
fun Key.toScafall() : com.wolfyscript.scafall.identifier.Key = ScafallProvider.get().factories.identifierFactory.key(this.namespace(), this.value())

/**
 * Converts a minecraft [ResourceLocation] to a scafall [com.wolfyscript.scafall.identifier.Key]
 */
fun Identifier.toScafall() : com.wolfyscript.scafall.identifier.Key = ScafallProvider.get().factories.identifierFactory.key(this.namespace, this.path)