package ru.rpuxa.core

import java.security.SecureRandom
import java.util.*

val secureRandom = SecureRandom()
val random = Random()

fun Random.nextInt(from: Int, to: Int) = from + nextInt(to - from)

operator fun StringBuilder.plusAssign(char: Char) {
    append(char)
}