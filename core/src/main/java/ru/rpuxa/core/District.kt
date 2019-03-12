package ru.rpuxa.core

open class District(
    open val id: Int,
    val name: String,
    val lvlNeeded: Int,
    val completeRubles: Int,
    val completeAuthority: Int
)