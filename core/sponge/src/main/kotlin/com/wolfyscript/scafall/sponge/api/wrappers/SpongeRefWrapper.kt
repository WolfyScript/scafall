package com.wolfyscript.scafall.sponge.api.wrappers

import java.lang.ref.WeakReference

open class SpongeRefWrapper<T> (original: T) {

    val ref: WeakReference<T> = WeakReference(original) // Do not hold on to the entity objects, so they are properly garbage-collected.

}