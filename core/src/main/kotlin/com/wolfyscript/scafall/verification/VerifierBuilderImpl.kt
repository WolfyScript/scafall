package com.wolfyscript.scafall.verification

import com.wolfyscript.scafall.identifier.Key
import java.util.function.Consumer
import java.util.function.Function

internal abstract class VerifierBuilderImpl<T : Any, B : VerifierBuilder<T, B, R>, R : Verifier<T>>(
    protected val key: Key?, protected val parentBuilder: VerifierBuilder<*, *, *>?
) : VerifierBuilder<T, B, R> {
    protected var validationFunction: Consumer<VerificationResult.Builder<T>>? = null
    protected val childValidators: MutableList<VerifierEntry<T, *>> = ArrayList()
    protected var required: Boolean = true
    protected var requiresOptionals: Int = 0
    protected var nameConstructorFunction: Function<VerificationResult<T>, String> =
        Function { container: VerificationResult<T> ->
            container.value().map { value: T -> value::class.java.simpleName }.orElse("Unnamed")
        }

    protected abstract fun self(): B

    override fun validate(validateFunction: Consumer<VerificationResult.Builder<T>>): B {
        this.validationFunction = validateFunction
        return self()
    }

    override fun optional(): B {
        this.required = false
        return self()
    }

    override fun name(nameConstructor: Function<VerificationResult<T>, String>): B {
        this.nameConstructorFunction = nameConstructor
        return self()
    }

    override fun require(count: Int): B {
        this.requiresOptionals = count
        return self()
    }

    override fun <C : Any> `object`(getter: Function<T, C>, verifier: ObjectVerifier<C>): B {
        return `object`(getter, verifier) { }
    }

    override fun <C : Any> `object`(
        getter: Function<T, C>,
        verifier: ObjectVerifier<C>,
        override: Consumer<ObjectVerifierBuilder<C>>
    ): B {
        val builderComplete = ObjectVerifierBuilderImpl(Key.key(Key.SCAFFOLDING_NAMESPACE, "default"), this, verifier)
        override.accept(builderComplete)
        childValidators.add(VerifierEntry(builderComplete.build(), getter))
        return self()
    }

    override fun <C : Any> `object`(getter: Function<T, C>, childBuilder: Consumer<ObjectVerifierBuilder<C>>): B {
        val builderComplete = ObjectVerifierBuilderImpl<C>(Key.key(Key.SCAFFOLDING_NAMESPACE, "default"), this)
        childBuilder.accept(builderComplete)
        childValidators.add(VerifierEntry(builderComplete.build(), getter))
        return self()
    }

    override fun <C : Any> collection(
        getter: Function<T, Collection<C>>,
        childBuilder: Consumer<CollectionVerifierBuilder<C>>
    ): B {
        val builderComplete = CollectionVerifierBuilderImpl<C>(Key.key(Key.SCAFFOLDING_NAMESPACE, "default"), this)
        childBuilder.accept(builderComplete)
        childValidators.add(VerifierEntry(builderComplete.build(), getter))
        return self()
    }
}
