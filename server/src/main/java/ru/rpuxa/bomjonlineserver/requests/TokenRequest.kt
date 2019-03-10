package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.Request
import ru.rpuxa.bomjonlineserver.dataBase
import ru.rpuxa.core.RequestCodes.BAD_ARGUMENTS
import ru.rpuxa.core.RequestCodes.UNKNOWN_TOKEN
import ru.rpuxa.core.UserData

abstract class TokenRequest(name: String) : Request(name) {

    override fun request(query: Query): String {
        val token = query["token"] ?: return BAD_ARGUMENTS.string
        val user = dataBase.userByToken(token) ?: return UNKNOWN_TOKEN.string
        return request(user, query)
    }

    protected abstract fun request(user: UserData, query: Query): String
}