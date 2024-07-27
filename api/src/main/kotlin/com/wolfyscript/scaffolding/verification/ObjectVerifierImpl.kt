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
