package com.wolfyscript.scafall.wrappers.world

class Color {

    companion object {
        const val BIT_MASK: Int = 0xff // Keeps the Integer values between 0 - 255

        fun fromRGB(hex: Int): Color {
            return Color(
                (hex shr 16) and BIT_MASK,
                (hex shr 8) and BIT_MASK,
                hex and BIT_MASK
            )
        }

        fun fromARGB(hex: Int): Color {
            return Color(
                (hex shr 16) and BIT_MASK,
                (hex shr 8) and BIT_MASK,
                hex and BIT_MASK,
                (hex shr 24) and BIT_MASK
            )
        }

    }

    val red: UByte
    val green: UByte
    val blue: UByte
    val alpha: UByte
    val rgb: Int
    val argb: Int

    constructor(red: Int, green: Int, blue: Int, alpha: Int = 255) : this(
        red.toUByte(),
        green.toUByte(),
        blue.toUByte(),
        alpha.toUByte()
    )

    constructor(red: UByte, green: UByte, blue: UByte, alpha: UByte = 255u) {
        this.red = red
        this.green = green
        this.blue = blue
        this.alpha = alpha

        // 0x00RRGGBB
        this.rgb = ((this.red.toInt() and BIT_MASK) shl 16) or
                ((this.green.toInt() and BIT_MASK) shl 8) or
                ((this.blue.toInt() and BIT_MASK) shl 0)
        // 0xAARRGGBB
        this.argb = ((this.alpha.toInt() and BIT_MASK) shl 24) or (this.rgb)
    }


}