package com.wolfyscript.scafall.verification

import java.util.function.Function

@JvmRecord
data class VerifierEntry<S, T>(val verifier: Verifier<T>, val valueGetter: Function<S, T>) {
    fun applyNestedValidator(source: S): VerificationResult<T> {
        return verifier.validate(valueGetter.apply(source))
    }

    override fun toString(): String {
        return "ValidatorEntry{" +
                "validator=" + verifier +
                '}'
    }
}
