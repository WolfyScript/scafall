package com.wolfyscript.scafall.platform

enum class PlatformType {

    SPIGOT,
    PAPER,
    PURPUR,
    FOLIA,
    SPONGE,
    FABRIC;

    fun isPaperCompatible(): Boolean {
        return when(this) {
            PAPER, FOLIA, PURPUR -> true
            else -> false
        }
    }

}