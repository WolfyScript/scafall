package com.wolfyscript.scaffolding.verification

interface CollectionVerifier<T> : Verifier<Collection<T>> {

    val elementVerifier: Verifier<T>

}
