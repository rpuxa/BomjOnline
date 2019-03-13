package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.toJson
import ru.rpuxa.core.UserAnswer
import ru.rpuxa.core.UserData

object UserRequest : TokenRequest("user") {
    override fun request(user: UserData, query: Query): String {
        return UserAnswer(user).toJson()
    }
}