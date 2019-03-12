package ru.rpuxa.bomjonlineserver.actions

import ru.rpuxa.core.District
import ru.rpuxa.core.DistrictAction
import java.util.*
import kotlin.collections.HashMap

object DistrictActions {

    val list: List<DistrictAction> get() = _list

    val DISTRICTS: List<District> = listOf(
        District(0, "Помойка", 0, 500, 600)
    )

    fun getDirectActionById(id: Int) = map[id]


    private val map = HashMap<Int, DistrictAction>()
    private val _list = ArrayList<DistrictAction>()

    init {
        direct(0) {
            add("Искать монеты во дворе", 10, 10)
            add("Искать монеты у остановок", 40, 45)
            add("Лазить по помойкам", 65, 75)
        }
    }

    private inline fun direct(id: Int, block: DirectBuilder.() -> Unit) {
        DirectBuilder(id).block()
    }

    private var actionId = 0

    private class DirectBuilder(val id: Int) {
        fun add(name: String, energyRemove: Int, rublesAdd: Int, count: Int = 14, authorityAdd: Int = 0) {
            _list.add(
                DistrictAction(
                    actionId++,
                    id,
                    name,
                    energyRemove,
                    authorityAdd.toLong(),
                    rublesAdd.toLong(),
                    count
                )
            )
        }
    }
}