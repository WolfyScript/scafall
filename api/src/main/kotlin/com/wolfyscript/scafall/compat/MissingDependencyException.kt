package com.wolfyscript.scafall.compat

class MissingDependencyException : RuntimeException {

    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)

}
