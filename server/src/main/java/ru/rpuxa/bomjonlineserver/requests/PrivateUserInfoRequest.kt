package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.toJson
import ru.rpuxa.core.UserData

object PrivateUserInfoRequest : TokenRequest("private_user_info") {
    override fun request(user: UserData, query: Query): String {
        return user.toJson()
    }
}