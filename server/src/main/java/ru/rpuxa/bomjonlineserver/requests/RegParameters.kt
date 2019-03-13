package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.Request
import ru.rpuxa.bomjonlineserver.toJson
import ru.rpuxa.core.RegParamsAnswer

object RegParameters : Request("regParams") {
    override fun request(query: Query): String {
        return ANSWER
    }


    const val LOGIN_MIN_LENGTH = 4
    const val LOGIN_MAX_LENGTH = 16
    const val LOGIN_SYMBOLS = "0-9a-zA-Zа-яА-Я_-"
    const val PASSWORD_MIN_LENGTH = 5
    const val PASSWORD_MAX_LENGTH = 16

    @JvmStatic
    private val ANSWER = RegParamsAnswer(
        LOGIN_MIN_LENGTH,
        LOGIN_MAX_LENGTH,
        LOGIN_SYMBOLS,
        PASSWORD_MIN_LENGTH,
        PASSWORD_MAX_LENGTH
    ).toJson()
}