package com.wolfyscript.scafall.verification

/**
 * Interface defining a contract for validating values of type T.
 * A Verifier is responsible for performing validation checks on a given value and returning a VerificationResult that encapsulates the outcome of the validation, including any faults
 *  or child validations.
 */
interface Verifier<T> {

    /**
     * Validates the provided value of type T and returns a VerificationResult that encapsulates the outcome of the validation.
     *
     * @param value The value to be validated of generic type [T]
     * @return A VerificationResult instance containing the result of the validation
     */
    fun validate(value: T): VerificationResult<T>

    /**
     * Indicates whether the validation is optional.
     *
     * @return `true` if the validation can be skipped; otherwise, `false`.
     */
    fun optional(): Boolean

    /**
     * Returns a string name for the given VerificationResult container.
     *
     * @param container The VerificationResult instance that holds the result of a validation operation.
     * @return A string representing the name associated with the provided VerificationResult container.
     */
    fun getNameFor(container: VerificationResult<T>): String
}
