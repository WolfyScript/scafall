package com.wolfyscript.scafall.function

fun interface ReceiverBiFunction<T, U, V> {

    fun T.apply(value: U) : V

}