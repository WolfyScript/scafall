package com.wolfyscript.scafall.function

fun interface ReceiverConsumer<T> {
    fun T.consume()
}