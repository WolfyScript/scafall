package com.wolfyscript.scafall.verification

import com.wolfyscript.scafall.identifier.Key
import java.util.*
import java.util.function.Consumer
import java.util.function.Function

internal class ObjectVerifierImpl<T_VALUE : Any>(
    private val key: Key,
    val required: Boolean,
    val requiredOptional: Int,
    var nameConstructorFunction: Function<VerificationResult<T_VALUE>, String>,
    val resultFunction: Consumer<VerificationResult.Builder<T_VALUE>>?,
    val childValidators: List<VerifierEntry<T_VALUE, *>>
) : ObjectVerifier<T_VALUE> {

    override fun optional(): Boolean {
        return !required
    }

    override fun getNameFor(container: VerificationResult<T_VALUE>): String {
        return nameConstructorFunction.apply(container)
    }

    override fun validate(value: T_VALUE): VerificationResult<T_VALUE> {
        val container = VerificationResultImpl.BuilderImpl(this, value)

        var requiredType: VerificationResult.ResultType = VerificationResult.ResultType.UNKNOWN
        val optionalCounts = EnumMap<VerificationResult.ResultType, Int>(
            VerificationResult.ResultType::class.java
        )

        for (entry in childValidators) {
            val result = entry.applyNestedValidator(value)
            container.children(listOf(result))
            if (entry.verifier.optional()) {
                optionalCounts.merge(result.type(), 1) { a: Int?, b: Int? ->
                    Integer.sum(
                        a!!, b!!
                    )
                }
                continue
            }
            requiredType = requiredType.and(result.type())
        }
        requiredType = if (optionalCounts.getOrDefault(VerificationResult.ResultType.VALID, 0) >= requiredOptional) {
            requiredType.and(VerificationResult.ResultType.VALID)
        } else {
            requiredType.and(VerificationResult.ResultType.INVALID)
        }

        container.type(requiredType)

        resultFunction?.accept(container)

        return container.complete()
    }

    override fun toString(): String {
        return "ValidatorImpl{" +
                "key=" + key +
                '}'
    }

    override fun key(): Key {
        return key
    }
}
