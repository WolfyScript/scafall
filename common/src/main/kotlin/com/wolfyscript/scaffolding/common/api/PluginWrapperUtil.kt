package com.wolfyscript.scafall.common.api

import com.wolfyscript.scafall.PluginWrapper

inline fun <reified T : PluginWrapper> PluginWrapper.into(): T {
    if (this is T) {
        return this
    }
    throw IllegalArgumentException("Unsupported plugin wrapper type: ${this::class.simpleName}")
}
