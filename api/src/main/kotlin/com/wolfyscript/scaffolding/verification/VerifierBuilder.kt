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

interface VerifierBuilder<T : Any, B : VerifierBuilder<T, B, R>, R : Verifier<T>> {
    /**
     * Specifies the validation function that validates the value.
     *
     * @param validateFunction The validation function
     * @return This builder instance for chaining
     */
    fun validate(validateFunction: Consumer<VerificationResult.Builder<T>>): B

    fun name(nameConstructor: Function<VerificationResult<T>, String>): B

    fun name(name: String): B {
        return name { tVerifierContainer: VerificationResult<T> -> name }
    }

    fun optional(): B

    fun require(count: Int): B

    /**
     * Adds a nested child object validation to this validator.
     * The getter provides a way to compute the child value from the current value.
     *
     * @param getter       The getter to get the child value
     * @param childBuilder The builder for the child validator
     * @param <C>          The child value type
     * @return This builder instance for chaining
    </C> */
    fun <C : Any> `object`(getter: Function<T, C>, childBuilder: Consumer<ObjectVerifierBuilder<C>>): B

    fun <C : Any> `object`(getter: Function<T, C>, verifier: ObjectVerifier<C>): B {
        return `object`(getter, verifier, Consumer { cVerifierBuilder: ObjectVerifierBuilder<C> -> })
    }

    fun <C : Any> `object`(
        getter: Function<T, C>,
        verifier: ObjectVerifier<C>,
        override: Consumer<ObjectVerifierBuilder<C>>
    ): B

    /**
     * Adds a nested child collection validator. The getter provides a way to compute the collection from the current value.
     *
     * @param getter       The getter to get the collection
     * @param childBuilder The builder for the child validator
     * @param <C>          The type of the collection elements
     * @return This builder for chaining
    </C> */
    fun <C : Any> collection(getter: Function<T, Collection<C>>, childBuilder: Consumer<CollectionVerifierBuilder<C>>): B

    fun build(): R

    companion object {
        /**
         * Initiates the builder for object validation
         *
         * @param <T> The type of the object
         * @return The init step for the validator builder
        </T> */
        fun <T : Any> obj(key: Key): ObjectVerifierBuilder<T> {
            return ObjectVerifierBuilderImpl(key, null)
        }

        fun <T : Any> obj(key: Key, extend: ObjectVerifier<T>): ObjectVerifierBuilder<T> {
            return ObjectVerifierBuilderImpl(key, null, extend)
        }

        /**
         * Initiates the builder for collection validation
         *
         * @param <T> The type of the object contained in the collection
         * @return This init step for the validator builder
        </T> */
        fun <T : Any> collection(key: Key): CollectionVerifierBuilder<T> {
            return CollectionVerifierBuilderImpl(key, null)
        }

        fun <T : Any> collection(key: Key, extend: CollectionVerifier<T>): CollectionVerifierBuilder<T> {
            return CollectionVerifierBuilderImpl(key, null, extend)
        }
    }
}
