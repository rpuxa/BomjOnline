package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.Request
import ru.rpuxa.bomjonlineserver.actions.DirectActions
import ru.rpuxa.bomjonlineserver.toJson
import ru.rpuxa.core.GameData

object UpdateRequest : Request("update") {
    override fun request(query: Query): String {
        return UPDATE_STRING
    }

    @JvmStatic
    val UPDATE_HASH: Int

    @JvmStatic
    private val UPDATE_STRING = GameData(DirectActions.list).apply {
        UPDATE_HASH = hash()
    }.toJson()
}