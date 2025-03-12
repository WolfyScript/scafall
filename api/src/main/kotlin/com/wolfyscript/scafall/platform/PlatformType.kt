package com.wolfyscript.scafall.platform

enum class PlatformType {

    SPIGOT,
    PAPER,
    PURPUR,
    FOLIA,
    SPONGE;

    fun isPaperCompatible() {
        when(this) {
            PAPER, FOLIA, PURPUR -> true
            else -> false
        }
    }

}