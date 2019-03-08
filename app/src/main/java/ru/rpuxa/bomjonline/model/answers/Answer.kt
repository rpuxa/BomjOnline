package ru.rpuxa.bomjonline.model.answers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class Answer {

    @Expose
    @SerializedName("error_code")
    val errorCode = 0

    val isError: Boolean get() = errorCode != 0
}