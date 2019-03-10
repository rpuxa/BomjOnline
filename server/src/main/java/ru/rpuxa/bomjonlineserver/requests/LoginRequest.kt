package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.Request
import ru.rpuxa.bomjonlineserver.dataBase
import ru.rpuxa.bomjonlineserver.toJson
import ru.rpuxa.core.RequestCodes.BAD_ARGUMENTS
import ru.rpuxa.core.RequestCodes.INCORRECT_LOGIN_OR_PASSWORD

object LoginRequest : Request("login") {
    override fun request(query: Query): String {
        val login = query["login"] ?: return BAD_ARGUMENTS.string
        val password = query["password"] ?: return BAD_ARGUMENTS.string

        val user = dataBase.userByLogin(login) ?: return INCORRECT_LOGIN_OR_PASSWORD.string
        if (user.password != password)
            return INCORRECT_LOGIN_OR_PASSWORD.string

        return ("token" to user.token).toJson()
    }


}