package com.wolfyscript.scafall.verification

import com.wolfyscript.scafall.identifier.Key
import java.util.*

internal class ObjectVerifierBuilderImpl<T : Any>(key: Key, parent: VerifierBuilder<*, *, *>?) :
    VerifierBuilderImpl<T, ObjectVerifierBuilder<T>, ObjectVerifier<T>>(key, parent), ObjectVerifierBuilder<T> {
    override fun self(): ObjectVerifierBuilder<T> {
        return this
    }

    constructor(key: Key, parent: VerifierBuilder<*, *, *>?, other: ObjectVerifier<T>) : this(key, parent) {
        if (other !is ObjectVerifierImpl<T>) {
            return
        }
        this.validationFunction = other.resultFunction
        childValidators.addAll(other.childValidators)
        this.required = other.required
        this.requiresOptionals = other.requiredOptional
        this.nameConstructorFunction = other.nameConstructorFunction
    }

    override fun build(): ObjectVerifier<T> {
        return ObjectVerifierImpl(
            Key.key(Key.SCAFFOLDING_NAMESPACE, "default"),
            required,
            requiresOptionals,
            nameConstructorFunction,
            validationFunction,
            Collections.unmodifiableList(childValidators)
        )
    }
}
