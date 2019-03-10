package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.Request
import ru.rpuxa.bomjonlineserver.createNewUser
import ru.rpuxa.bomjonlineserver.dataBase
import ru.rpuxa.bomjonlineserver.toJson
import ru.rpuxa.core.RequestCodes.BAD_ARGUMENTS

object RegRequest : Request("reg") {
    override fun request(query: Query): String {
        val login = query["login"] ?: return BAD_ARGUMENTS.string
        val password = query["password"] ?: return BAD_ARGUMENTS.string
        val mail = query["email"] ?: return BAD_ARGUMENTS.string

        val error = CheckRegRequest.check(login, password, mail)
        if (error != null)
            return error

        val user = createNewUser(login, password, mail)
        dataBase.updateUser(user)

        return ("token" to user.token).toJson()
    }


}