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

import java.util.function.Consumer

interface CollectionVerifierBuilder<T : Any> : VerifierBuilder<Collection<T>, CollectionVerifierBuilder<T>, CollectionVerifier<T>> {

    /**
     * Specifies the validator that is used to validate each element in the collection.
     *
     * @param childBuilder The element validator builder
     * @return This build instance for chaining
     */
    fun forEach(childBuilder: Consumer<VerifierBuilder<T, *, *>>): CollectionVerifierBuilder<T>

    fun forEach(existing: Verifier<T>): CollectionVerifierBuilder<T>
}
