package com.wolfyscript.scafall.verification

import java.util.*
import java.util.function.Consumer

interface VerificationResult<T> {

    fun children(): List<VerificationResult<*>>

    fun optional(): Boolean

    val name: String

    fun value(): Optional<T>

    fun type(): ResultType

    fun faults(): Collection<String>

    fun printToOut(level: Int, prefix: String, out: Consumer<String>) {
        printToOut(level, true, prefix, out)
    }

    fun printToOut(level: Int, printName: Boolean, prefix: String, out: Consumer<String>)

    interface Builder<T : Any> {

        fun currentValue(): Optional<T>

        fun currentType(): ResultType

        fun fault(message: String): Builder<T>

        fun clearFaults(): Builder<T>

        fun valid(): Builder<T>

        fun invalid(): Builder<T>

        fun type(type: ResultType): Builder<T>

        fun children(children: List<VerificationResult<*>>): Builder<T>

        fun complete(): VerificationResult<T>
    }

    enum class ResultType {
        UNKNOWN,
        VALID,
        INVALID;

        fun and(other: ResultType): ResultType {
            if (this == UNKNOWN) return other
            return if (this == VALID) other else this
        }

        val isValid: Boolean
            get() = this == VALID
    }
}
