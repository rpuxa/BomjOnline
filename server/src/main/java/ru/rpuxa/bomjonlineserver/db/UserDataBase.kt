package ru.rpuxa.bomjonlineserver.db

import ru.rpuxa.core.UserData

interface UserDataBase {

    fun updateUser(user: UserData)

    fun nextRandomId(): Int

    fun userByToken(token: String): UserData?

    fun userByLogin(login: String): UserData?

    fun userByMail(mail: String): UserData?

    fun userById(id: Int): UserData?
}

inline fun UserDataBase.updateUser(user: UserData, block: (UserData) -> Unit) {
    block(user)
    updateUser(user)
}