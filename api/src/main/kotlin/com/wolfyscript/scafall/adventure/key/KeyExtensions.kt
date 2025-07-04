package com.wolfyscript.scafall.adventure.key

import com.wolfyscript.scafall.ScafallProvider
import net.kyori.adventure.key.Key

fun Key.toScafall() : com.wolfyscript.scafall.identifier.Key = ScafallProvider.Companion.get().factories.identifierFactory.key(this.namespace(), this.value())
