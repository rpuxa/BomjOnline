package ru.rpuxa.bomjonlineserver.db

import ru.rpuxa.core.UserData
import ru.rpuxa.core.random

class ListUserDataBase : UserDataBase {
    private val list = ArrayList<UserData>()

    override fun updateUser(user: UserData) {
        if (!list.any { it.id == user.id })
            list.add(user)
    }

    override fun nextRandomId(): Int {
        return random.nextInt()
    }

    override fun userByLogin(login: String) = list.find { it.login == login }

    override fun userByMail(mail: String) = list.find { it.email == mail }

    override fun userByToken(token: String) = list.find { it.token == token }

    override fun userById(id: Int) = list.find { it.id == id }
}