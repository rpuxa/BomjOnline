package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.Request
import ru.rpuxa.bomjonlineserver.toJson

object RegParameters : Request("reg_params") {
    override fun request(query: Query): String {
        return ANSWER
    }


    const val LOGIN_MIN_LENGTH = 4
    const val LOGIN_MAX_LENGTH = 16
    const val LOGIN_SYMBOLS = "0-9a-zA-Zа-яА-Я_-"
    const val PASSWORD_MIN_LENGTH = 5
    const val PASSWORD_MAX_LENGTH = 16

    @JvmStatic
    private val ANSWER = mapOf(
        "login_min_length" to LOGIN_MIN_LENGTH,
        "login_max_length" to LOGIN_MAX_LENGTH,
        "login_symbols" to LOGIN_SYMBOLS,
        "password_min_length" to PASSWORD_MIN_LENGTH,
        "password_max_length" to PASSWORD_MAX_LENGTH
    ).toJson()
}