package com.wolfyscript.scafall.verification

import java.util.function.Consumer

interface CollectionVerifierBuilder<T : Any> : VerifierBuilder<Collection<T>, CollectionVerifierBuilder<T>, CollectionVerifier<T>> {

    /**
     * Specifies the validator that is used to validate each element in the collection.
     *
     * @param childBuilder The element validator builder
     * @return This build instance for chaining
     */
    fun forEach(childBuilder: Consumer<VerifierBuilder<T, *, *>>): CollectionVerifierBuilder<T>

    fun forEach(existing: Verifier<T>): CollectionVerifierBuilder<T>
}
