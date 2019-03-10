package ru.rpuxa.core

import java.util.*

class GameData(
    val directActions: List<DirectAction>
) {
    fun hash(): Int = Objects.hash(
        directActions
    )
}
