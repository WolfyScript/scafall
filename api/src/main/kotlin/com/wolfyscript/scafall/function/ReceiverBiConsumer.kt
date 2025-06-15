package com.wolfyscript.scafall.function

fun interface ReceiverBiConsumer<T, U> {
    fun T.consume(value: U)
}