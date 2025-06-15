package com.wolfyscript.scafall.function

fun interface ReceiverFunction<T, U> {

    fun T.apply() : U

}