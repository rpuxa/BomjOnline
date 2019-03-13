package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.Request
import ru.rpuxa.bomjonlineserver.dataBase
import ru.rpuxa.bomjonlineserver.toJson
import ru.rpuxa.core.PublicUserAnswer
import ru.rpuxa.core.RequestCodes

object PublicUserRequest : Request("publicUser") {
    override fun request(query: Query): String {
        val id = query["id"]?.toIntOrNull() ?: return RequestCodes.BAD_ARGUMENTS.string
        val user = dataBase.userById(id) ?: return RequestCodes.WRONG_USER_ID.string

        return PublicUserAnswer(user.toPublicUser()).toJson()
    }
}