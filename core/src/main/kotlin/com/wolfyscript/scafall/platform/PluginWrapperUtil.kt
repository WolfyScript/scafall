package com.wolfyscript.scafall.platform

import com.wolfyscript.scafall.ModWrapper

inline fun <reified T : ModWrapper> ModWrapper.into(): T {
    if (this is T) {
        return this
    }
    throw IllegalArgumentException("Unsupported plugin wrapper type: ${this::class.simpleName}")
}
