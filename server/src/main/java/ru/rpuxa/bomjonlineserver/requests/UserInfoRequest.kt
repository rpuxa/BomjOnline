package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.Request
import ru.rpuxa.bomjonlineserver.dataBase
import ru.rpuxa.bomjonlineserver.toJson
import ru.rpuxa.core.RequestCodes

object UserInfoRequest : Request("user_info") {
    override fun request(query: Query): String {
        val id = query["id"]?.toIntOrNull() ?: return RequestCodes.BAD_ARGUMENTS.string
        val user = dataBase.userById(id) ?: return RequestCodes.WRONG_USER_ID.string

        return user.toPublicUser().toJson()
    }
}