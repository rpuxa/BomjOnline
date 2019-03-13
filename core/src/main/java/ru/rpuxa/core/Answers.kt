package ru.rpuxa.core

open class Answer {
    val errorCode = 0

    val isError: Boolean get() = errorCode != 0
}

class LoginAnswer(val token: String) : Answer()

class RegParamsAnswer(
    val loginMinLength: Int,
    val loginMaxLength: Int,
    val loginSymbols: String,
    val passwordMinLength: Int,
    val passwordMaxLength: Int
) : Answer()

class UpdateRequired(val required: Int) : Answer()

class UserAnswer(val user: UserData) : Answer()

class PublicUserAnswer(val user: PublicUserData) : Answer()