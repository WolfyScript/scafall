package com.wolfyscript.scafall.eval.value_provider

fun String.provider() : ValueProvider<String> {
    return ValueProviderStringConst(this)
}

fun Int.provider() : ValueProvider<Int> {
    return ValueProviderIntegerConst(this)
}

fun Long.provider() : ValueProvider<Long> {
    return ValueProviderLongConst(this)
}

fun Byte.provider() : ValueProvider<Byte> {
    return ValueProviderByteConst(this)
}

fun Short.provider() : ValueProvider<Short> {
    return ValueProviderShortConst(this)
}
