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
