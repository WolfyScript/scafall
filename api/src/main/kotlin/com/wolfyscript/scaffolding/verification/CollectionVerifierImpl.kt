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

internal class CollectionVerifierImpl<T : Any>(
    private val key: Key,
    val required: Boolean,
    val requiredOptional: Int,
    var nameConstructorFunction: Function<VerificationResult<Collection<T>>, String>,
    val resultFunction: Consumer<VerificationResult.Builder<Collection<T>>>?,
    override val elementVerifier: Verifier<T>
) : CollectionVerifier<T> {
    override fun getNameFor(container: VerificationResult<Collection<T>>): String {
        return nameConstructorFunction.apply(container)
    }

    override fun validate(values: Collection<T>): VerificationResult<Collection<T>> {
        val container = VerificationResultImpl.BuilderImpl(this, values)

        val resultType: VerificationResult.ResultType
        if (elementVerifier.optional()) {
            val counts: MutableMap<VerificationResult.ResultType, Int> = EnumMap(
                VerificationResult.ResultType::class.java
            )
            for (value in values) {
                val result = elementVerifier.validate(value)
                container.children(listOf<VerificationResult<*>>(result))
                counts.merge(result.type(), 1) { a, b ->
                    Integer.sum(a, b)
                }
            }

            resultType = if (counts.getOrDefault(VerificationResult.ResultType.VALID, 0) >= requiredOptional) {
                VerificationResult.ResultType.VALID
            } else {
                VerificationResult.ResultType.INVALID
            }
        } else {
            resultType = values.stream()
                .map { value: T ->
                    val result = elementVerifier.validate(value)
                    container.children(listOf<VerificationResult<*>>(result))
                    result.type()
                }
                .distinct()
                .reduce { it, other -> it.and(other) }
                .orElse(VerificationResult.ResultType.INVALID)
        }

        container.type(resultType)

        resultFunction?.accept(container)

        return container.complete()
    }

    override fun optional(): Boolean {
        return !required
    }

    override fun key(): Key {
        return key
    }
}
