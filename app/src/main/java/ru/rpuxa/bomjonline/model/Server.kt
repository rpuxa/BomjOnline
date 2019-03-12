package ru.rpuxa.bomjonline.model

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.rpuxa.bomjonline.model.answers.Answer
import ru.rpuxa.bomjonline.model.answers.LoginAnswer
import ru.rpuxa.bomjonline.model.answers.RegParamsAnswer
import ru.rpuxa.bomjonline.model.answers.UpdateRequired
import ru.rpuxa.core.GameData
import ru.rpuxa.core.PublicUserData
import ru.rpuxa.core.UserData

interface Server {

    @GET("login")
    fun login(
        @Query("login") login: String,
        @Query("password") password: String
    ): Call<LoginAnswer>

    @GET("reg")
    fun reg(
        @Query("login") login: String,
        @Query("password") password: String,
        @Query("email") email: String
    ): Call<LoginAnswer>

    @GET("check_reg")
    fun checkReg(
        @Query("login") login: String = NULL,
        @Query("password") password: String = NULL,
        @Query("email") email: String = NULL
    ): Call<Answer>

    @GET("reg_params")
    fun regParams(): Call<RegParamsAnswer>

    @GET("check_update")
    fun checkUpdate(@Query("hash") hash: Int): Call<UpdateRequired>

    @POST("update")
    fun update(): Call<GameData>

    @POST("private_user_info")
    fun getUserData(@Query("token") token: String): Call<UserData>

    @GET("user_info")
    fun getUserData(@Query("id") id: Int): Call<PublicUserData>

    @GET("make_direct_action")
    fun makeDirectAction(
        @Query("token") token: String,
        @Query("id") id: Int
    ): Call<Answer>


    companion object {
        private const val IP = "http://192.168.137.1"
        private const val API_ADDRESS = "$IP/api/"
        private const val NULL = "<NULL>"

        fun create(): Server =
            Retrofit.Builder()
                .baseUrl(API_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Server::class.java)
    }

}