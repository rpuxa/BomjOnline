package ru.rpuxa.core

import java.util.*

class GameData(
    val districtsActions: List<DistrictAction>,
    val districts: List<District>
) {
    fun hash(): Int = Objects.hash(
        districtsActions, districts
    )
}
