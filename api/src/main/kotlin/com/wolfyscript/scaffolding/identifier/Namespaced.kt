package com.wolfyscript.scaffolding.identifier

import org.intellij.lang.annotations.Pattern

interface Namespaced {

    @get:Pattern("[a-z0-9_\\-.]+")
    val namespace: String

}