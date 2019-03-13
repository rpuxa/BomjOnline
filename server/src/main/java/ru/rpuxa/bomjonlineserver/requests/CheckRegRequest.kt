package ru.rpuxa.bomjonlineserver.requests

import ru.rpuxa.bomjonlineserver.Request
import ru.rpuxa.bomjonlineserver.dataBase
import ru.rpuxa.bomjonlineserver.requests.RegParameters.LOGIN_MAX_LENGTH
import ru.rpuxa.bomjonlineserver.requests.RegParameters.LOGIN_MIN_LENGTH
import ru.rpuxa.bomjonlineserver.requests.RegParameters.PASSWORD_MAX_LENGTH
import ru.rpuxa.bomjonlineserver.requests.RegParameters.PASSWORD_MIN_LENGTH
import ru.rpuxa.core.RequestCodes
import ru.rpuxa.core.RequestCodes.LOGIN_IS_TOO_SHORT

object CheckRegRequest : Request("checkReg") {
    override fun request(query: Request.Query): String {
        val login = query["login"]
        val password = query["password"]
        val mail = query["email"]

        if (query.isEmpty())
            return RequestCodes.BAD_ARGUMENTS.string

        return check(login, password, mail) ?: RequestCodes.NO_ERROR.string
    }

    fun check(login: String?, password: String?, mail: String?): String? {
        if (login != null) {
            if (login.length < LOGIN_MIN_LENGTH)
                return LOGIN_IS_TOO_SHORT.string
            if (login.length > LOGIN_MAX_LENGTH)
                return RequestCodes.LOGIN_IS_TOO_LONG.string
            if (!login.matches(Regex("[" + RegParameters.LOGIN_SYMBOLS + "]+")))
                return RequestCodes.WRONG_LOGIN_SYMBOLS.string
            if (dataBase.userByLogin(login) != null)
                return RequestCodes.LOGIN_ALREADY_EXISTS.string
        }

        if (password != null) {
            if (password.length < PASSWORD_MIN_LENGTH)
                return RequestCodes.PASSWORD_IS_TOO_SHORT.string
            if (password.length > PASSWORD_MAX_LENGTH)
                return RequestCodes.PASSWORD_IS_TOO_LONG.string
        }

        if (mail != null) {
            if (!isEmail(mail))
                return RequestCodes.INCORRECT_EMAIL.string
            if (dataBase.userByMail(mail) != null)
                return RequestCodes.EMAIL_ALREADY_USED.string
        }


        return null
    }


    private fun isEmail(email: String) = email.matches(EMAIL_REGEX)

    @JvmStatic
    private val EMAIL_REGEX = Regex("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
}