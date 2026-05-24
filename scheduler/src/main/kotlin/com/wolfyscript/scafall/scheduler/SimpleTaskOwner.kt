package com.wolfyscript.scafall.scheduler

import java.util.UUID

internal class SimpleTaskOwner(val id: UUID) : TaskOwner {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SimpleTaskOwner) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}