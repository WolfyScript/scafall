/*
 *       ____ _  _ ____ ___ ____ _  _ ____ ____ ____ ____ ___ _ _  _ ____
 *       |    |  | [__   |  |  | |\/| |    |__/ |__| |___  |  | |\ | | __
 *       |___ |__| ___]  |  |__| |  | |___ |  \ |  | |     |  | | \| |__]
 *
 *       CustomCrafting Recipe creation and management tool for Minecraft
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.wolfyscript.scaffolding.verification

import com.wolfyscript.scaffolding.identifier.Key
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
