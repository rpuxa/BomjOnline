package ru.rpuxa.bomjonlineserver

import com.google.gson.Gson


private val gson = Gson()

fun Pair<String, Any>.toJson() = mapOf(this).toJson()

fun Any.toJson(): String = gson.toJson(this)