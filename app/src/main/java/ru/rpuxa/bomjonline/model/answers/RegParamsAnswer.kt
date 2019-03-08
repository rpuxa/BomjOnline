package ru.rpuxa.bomjonline.model.answers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegParamsAnswer : Answer() {
    @Expose
    @SerializedName("login_min_length")
    val loginMinLength = -1

    @Expose
    @SerializedName("login_max_length")
    val loginMaxLength = -1

    @Expose
    @SerializedName("login_symbols")
    val loginSymbols = ""

    @Expose
    @SerializedName("password_min_length")
    val passwordMinLength = -1

    @Expose
    @SerializedName("password_max_length")
    val passwordMaxLength = -1
}