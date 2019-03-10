package ru.rpuxa.bomjonlineserver.actions

import ru.rpuxa.core.DirectAction
import java.util.*
import kotlin.collections.HashMap

object DirectActions {

    val list: List<DirectAction> get() = _list

    fun getDirectActionById(id: Int) = map[id]


    private val map = HashMap<Int, DirectAction>()
    private val _list = ArrayList<DirectAction>()

    init {
        direct(0) {
            add("Искать монеты во дворе", 10, 10)
            add("Искать монеты у остановок", 40, 45)
            add("Лазить по помойкам", 65, 75)
        }
    }

    private inline fun direct(id: Int, block: Direct.() -> Unit) {
        Direct(id).block()
    }

    private var actionId = 0

    private class Direct(val id: Int) {
        fun add(name: String, energyRemove: Int, rublesAdd: Int, authorityAdd: Int = 0) {
            _list.add(DirectAction(actionId++, id, name, energyRemove, authorityAdd.toLong(), rublesAdd.toLong()))
        }
    }
}