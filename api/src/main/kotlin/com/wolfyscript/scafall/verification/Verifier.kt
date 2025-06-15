package com.wolfyscript.scafall.verification

import com.wolfyscript.scafall.identifier.Keyed

interface Verifier<T> : Keyed {
    fun validate(value: T): VerificationResult<T>

    fun optional(): Boolean

    fun getNameFor(container: VerificationResult<T>): String
}
