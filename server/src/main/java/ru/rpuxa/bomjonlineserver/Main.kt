package ru.rpuxa.bomjonlineserver

import ru.rpuxa.bomjonlineserver.db.ListUserDataBase
import ru.rpuxa.bomjonlineserver.db.UserDataBase

val dataBase: UserDataBase = ListUserDataBase()

fun main() {
    HTTPServer().run()

}