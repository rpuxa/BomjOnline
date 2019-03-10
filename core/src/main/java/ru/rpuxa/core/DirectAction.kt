package ru.rpuxa.core

import java.util.*

@Suppress("EqualsOrHashCode")
class DirectAction(
    val id: Int,
    val directId: Int,
    val name: String,
    val energyRemove: Int,
    val authorityAdd: Long,
    val rublesAdd: Long
) {

    override fun hashCode(): Int =
        Objects.hash(directId, name, energyRemove, authorityAdd, rublesAdd)

}