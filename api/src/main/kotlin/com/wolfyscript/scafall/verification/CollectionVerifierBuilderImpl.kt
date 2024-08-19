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
package com.wolfyscript.scafall.verification

import com.wolfyscript.scafall.identifier.Key
import java.util.function.Consumer

internal class CollectionVerifierBuilderImpl<T : Any> :
    VerifierBuilderImpl<Collection<T>, CollectionVerifierBuilder<T>, CollectionVerifier<T>>,
    CollectionVerifierBuilder<T> {
    private var elementVerifier: Verifier<T>? = null

    constructor(key: Key, parent: VerifierBuilder<*, *, *>?) : super(key, parent)

    constructor(key: Key, parent: VerifierBuilder<*, *, *>?, other: CollectionVerifier<T>) : super(key, parent) {
        if (other !is CollectionVerifierImpl<T>) {
            return
        }
        this.elementVerifier = other.elementVerifier
        this.nameConstructorFunction = other.nameConstructorFunction
        this.validationFunction = other.resultFunction
        this.required = !other.optional()
        this.requiresOptionals = other.requiredOptional
    }

    override fun self(): CollectionVerifierBuilder<T> {
        return this
    }

    override fun forEach(childBuilder: Consumer<VerifierBuilder<T, *, *>>): CollectionVerifierBuilder<T> {
        val builder: ObjectVerifierBuilder<T> = VerifierBuilder.obj(Key.key(Key.SCAFFOLDING_NAMESPACE, "default"))
        childBuilder.accept(builder)
        elementVerifier = builder.build()
        return this
    }

    override fun forEach(existing: Verifier<T>): CollectionVerifierBuilder<T> {
        elementVerifier = existing
        return this
    }

    override fun build(): CollectionVerifier<T> {
        if (elementVerifier == null) {
            throw IllegalStateException("Element verifier has not been set")
        }

        return CollectionVerifierImpl(
                Key.key(Key.SCAFFOLDING_NAMESPACE, "default"),
                required,
                requiresOptionals,
                nameConstructorFunction,
                validationFunction,
                elementVerifier!!
            )
    }
}
