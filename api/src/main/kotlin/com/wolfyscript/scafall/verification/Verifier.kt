package com.wolfyscript.scafall.verification

interface Verifier<T> {
    fun validate(value: T): VerificationResult<T>

    fun optional(): Boolean

    fun getNameFor(container: VerificationResult<T>): String
}
