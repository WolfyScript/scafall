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
