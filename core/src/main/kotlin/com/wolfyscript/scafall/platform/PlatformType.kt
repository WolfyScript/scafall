package com.wolfyscript.scafall.platform

/**
 * Represents the different Minecraft server platforms that the software can operate on.
 * Each platform may have specific compatibility considerations, such as plugin APIs and performance optimizations.
 */
enum class PlatformType {

    SPIGOT,
    PAPER,
    PURPUR,
    FOLIA,
    SPONGE,
    FABRIC;

    /**
     * Determines if the current platform type is compatible with Paper or related server implementations.
     *
     * @return `true` if the platform type is compatible with Paper; otherwise, `false`.
     */
    fun isPaperCompatible(): Boolean {
        return when(this) {
            PAPER, FOLIA, PURPUR -> true
            else -> false
        }
    }

}