package com.wolfyscript.scafall.verification

interface CollectionVerifier<T> : Verifier<Collection<T>> {

    val elementVerifier: Verifier<T>

}
