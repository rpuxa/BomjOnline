package ru.rpuxa.bomjonlineserver

import ru.rpuxa.core.UserData
import ru.rpuxa.core.nextInt
import ru.rpuxa.core.plusAssign
import ru.rpuxa.core.secureRandom


fun createNewUser(login: String, password: String, mail: String): UserData =
    UserData(
        login,
        password,
        mail,
        dataBase.nextRandomId(),
        generateToken(),
        50,
        100,
        100,
        100,
        100,
        0,
        HashMap()
    )

private const val TOKEN_LENGTH = 32

private fun generateToken(): String {
    val builder = StringBuilder()
    repeat(TOKEN_LENGTH) {
        builder += if (secureRandom.nextBoolean()) {
            secureRandom.nextInt('a'.toInt(), 'z'.toInt()).toChar()
        } else {
            secureRandom.nextInt('A'.toInt(), 'Z'.toInt()).toChar()
        }
    }
    return builder.toString()
}
