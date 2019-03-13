package ru.rpuxa.core

import java.util.*

@Suppress("EqualsOrHashCode")
open class District(
    open val id: Int,
    val name: String,
    val lvlNeeded: Int,
    val completeRubles: Int,
    val completeAuthority: Int
) {
    override fun hashCode(): Int = Objects.hash(id, name, lvlNeeded, completeRubles, completeAuthority)
}