package ru.rpuxa.bomjonline.model.answers

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginAnswer : Answer() {
    @Expose
    @SerializedName("token")
    lateinit var token: String
        private set
}