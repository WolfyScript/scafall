package com.wolfyscript.scafall.platform

import com.wolfyscript.scafall.core.ModIdentifier

inline fun <reified T : ModIdentifier> ModIdentifier.into(): T {
    if (this is T) {
        return this
    }
    throw IllegalArgumentException("Unsupported plugin wrapper type: ${this::class.simpleName}")
}
