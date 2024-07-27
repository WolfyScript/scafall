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
import kotlin.math.max

internal class VerificationResultImpl<T : Any> private constructor(
    private val value: T?,
    private val verifier: Verifier<T>,
    private val type: VerificationResult.ResultType,
    faults: Set<String>,
    children: List<VerificationResult<*>>
) : VerificationResult<T> {
    private val faults: Set<String> = Collections.unmodifiableSet(faults)
    private val children: List<VerificationResult<*>> = Collections.unmodifiableList(children)

    override val name: String
        get() = verifier.getNameFor(this)

    override fun optional(): Boolean {
        return verifier.optional()
    }

    override fun children(): List<VerificationResult<*>> {
        return children
    }

    override fun value(): Optional<T> {
        return Optional.ofNullable(value)
    }

    override fun type(): VerificationResult.ResultType {
        return type
    }

    override fun faults(): Collection<String> {
        return faults
    }

    override fun equals(obj: Any?): Boolean {
        if (obj === this) return true
        if (obj == null || obj.javaClass != this.javaClass) return false
        val that = obj as VerificationResultImpl<*>
        return this.type == that.type && (this.faults == that.faults)
    }

    override fun hashCode(): Int {
        return Objects.hash(type, faults)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        printToOut(0, "") { string: String? -> sb.append(string).append('\n') }
        return sb.toString()
    }

    override fun printToOut(level: Int, printName: Boolean, prefix: String, out: Consumer<String>) {
        if (printName) {
            out.accept(prefix.substring(0, max(0.0, (prefix.length - 3).toDouble()).toInt()) + name)
        }

        for (fault in faults()) {
            out.accept("$prefix! $fault")
        }

        for (verificationResult in children) {
            if (verificationResult is VerificationResultImpl<*>) {
                if (verificationResult.type() == VerificationResult.ResultType.VALID) continue
                verificationResult.printToOut(level + 1, "$prefix   ", out)
            }
        }
    }

    class BuilderImpl<T : Any>(private val verifier: Verifier<T>, private val value: T?) : VerificationResult.Builder<T> {
        private var type: VerificationResult.ResultType = VerificationResult.ResultType.UNKNOWN
        private val faults: MutableSet<String> = HashSet()
        private val children: MutableList<VerificationResult<*>> = ArrayList()

        override fun currentValue(): Optional<T> {
            return Optional.ofNullable(value)
        }

        override fun currentType(): VerificationResult.ResultType {
            return type
        }

        override fun fault(message: String): VerificationResult.Builder<T> {
            faults.add(message)
            return this
        }

        override fun clearFaults(): VerificationResult.Builder<T> {
            faults.clear()
            return this
        }

        override fun valid(): VerificationResult.Builder<T> {
            return type(VerificationResult.ResultType.VALID)
        }

        override fun invalid(): VerificationResult.Builder<T> {
            return type(VerificationResult.ResultType.INVALID)
        }

        override fun type(type: VerificationResult.ResultType): VerificationResult.Builder<T> {
            this.type = type
            return this
        }

        override fun children(children: List<VerificationResult<*>>): VerificationResult.Builder<T> {
            this.children.addAll(children)
            return this
        }

        override fun complete(): VerificationResult<T> {
            return VerificationResultImpl(value, verifier, type, faults, children)
        }
    }
}

