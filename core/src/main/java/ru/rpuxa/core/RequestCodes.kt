package ru.rpuxa.core

enum class RequestCodes(val code: Int, val string: String = errorCode(code)) {
    NO_ERROR(0),
    BAD_ARGUMENTS(1),
    UNKNOWN_TOKEN(2),
    LOGIN_IS_TOO_SHORT(3),
    LOGIN_IS_TOO_LONG(4),
    WRONG_LOGIN_SYMBOLS(5),
    PASSWORD_IS_TOO_SHORT(6),
    PASSWORD_IS_TOO_LONG(7),
    INCORRECT_EMAIL(8),
    LOGIN_ALREADY_EXISTS(9),
    EMAIL_ALREADY_USED(10),
    INCORRECT_LOGIN_OR_PASSWORD(11),
    OUT_OF_SYNC(12),
    WRONG_ACTION_ID(13),
    WRONG_USER_ID(14)
    ;

}

private fun errorCode(code: Int) = "{\"errorCode\":$code}"

