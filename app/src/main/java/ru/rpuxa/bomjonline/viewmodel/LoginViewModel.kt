package ru.rpuxa.bomjonline.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.rpuxa.bomjonline.MutableLiveData
import ru.rpuxa.bomjonline.await
import ru.rpuxa.bomjonline.model.Model
import ru.rpuxa.bomjonline.model.answers.Answer
import ru.rpuxa.bomjonline.model.answers.RegParamsAnswer
import ru.rpuxa.bomjonline.postValue
import java.io.IOException

class LoginViewModel : ViewModel() {

    private val _regParams = MutableLiveData<RegParamsAnswer?>(null)
    val regParams: LiveData<RegParamsAnswer?> get() = _regParams

    private val _noInternetConnection = MutableLiveData(false)
    val noInternetConnection: LiveData<Boolean> get() = _noInternetConnection

    private val _loginStatus = MutableLiveData(LOGIN_EMPTY)
    val loginStatus: LiveData<Int> get() = _loginStatus

    private val _passwordStatus = MutableLiveData(PASSWORD_EMPTY)
    val passwordStatus: LiveData<Int> get() = _passwordStatus

    private val _emailStatus = MutableLiveData(EMAIL_EMPTY)
    val emailStatus: LiveData<Int> get() = _emailStatus

    val tokenStatus = MutableLiveData(TOKEN_EMPTY)

    var token = ""
        private set


    val loginToCheck = MutableLiveData<String?>(null)
    val passwordToCheck = MutableLiveData<String?>(null)
    val emailToCheck = MutableLiveData<String?>(null)


    init {
        loginToCheck.observeForever {
            checkLogin()
        }
        passwordToCheck.observeForever {
            checkPassword()
        }
        emailToCheck.observeForever {
            checkEmail()
        }
        loadRegParams()
    }

    fun loadRegParams() {
        trySendRequest {
            _regParams.postValue = Model.server.regParams().await()
            checkLogin()
            checkPassword()
            checkEmail()
        }
    }

    private fun checkLogin() {
        val login = loginToCheck.value
        if (login == null || login.isBlank()) {
            _loginStatus.postValue = LOGIN_EMPTY
            return
        }
        val regParams = _regParams.value
        if (regParams != null) {
            val error = when {
                login.length > regParams.loginMaxLength -> LOGIN_IS_TOO_LONG
                login.length < regParams.loginMinLength -> LOGIN_IS_TOO_SHORT
                !Regex("[${regParams.loginSymbols}]+" + "").matches(login) -> LOGIN_WRONG_SYMBOLS
                else -> -1
            }
            if (error != -1) {
                _loginStatus.postValue = error
                return
            }
        }
        _loginStatus.postValue = LOGIN_CHECKING
        if (regParams == null)
            return
        trySendRequest {
            val error = Model.server.checkReg(login = login).await()
            _loginStatus.postValue =
                if (error == RequestCodes.LOGIN_ALREADY_EXISTS) LOGIN_ALREADY_USED else {
                    unknownError(error)
                    LOGIN_AVAILABLE
                }
        }
    }

    private fun checkPassword() {
        val pass = passwordToCheck.value
        val regParams = _regParams.value
        if (pass == null || pass.isEmpty() || regParams == null) {
            _passwordStatus.postValue = PASSWORD_EMPTY
            return
        }
        val state = when {
            pass.length > regParams.passwordMaxLength -> PASSWORD_IS_TOO_LONG
            pass.length < regParams.passwordMinLength -> PASSWORD_IS_TOO_SHORT
            else -> PASSWORD_AVAILABLE
        }
        _passwordStatus.postValue = state
    }

    private fun checkEmail() {
        val email = emailToCheck.value
        if (email == null || email.isBlank()) {
            _emailStatus.postValue = EMAIL_EMPTY
            return
        }
        if (!isEmail(email)) {
            _emailStatus.postValue = EMAIL_WRONG
            return
        }
        _emailStatus.postValue = EMAIL_CHECKING
        trySendRequest {
            val error = Model.server.checkReg(email = email).await()
            _emailStatus.postValue =
                if (error.errorCode == RequestCodes.EMAIL_ALREADY_USED.code) EMAIL_ALREADY_USED else {
                    unknownError(error)
                    EMAIL_AVAILABLE
                }
        }
    }

    fun tryAgain() {
        _noInternetConnection.value = false
        if (regParams.value == null)
            loadRegParams()
        if (loginStatus.value != LOGIN_AVAILABLE)
            checkLogin()
        if (passwordStatus.value != PASSWORD_AVAILABLE)
            checkPassword()
        if (emailStatus.value != EMAIL_AVAILABLE)
            checkEmail()
        if (tokenStatus.value == TOKEN_GETTING)
            tokenStatus.value = TOKEN_EMPTY
    }

    fun register(login: String, password: String, email: String) {
        tokenStatus.value = TOKEN_GETTING
        trySendRequest {
            val ans = Model.server.reg(login, password, email).await()
            unknownError(ans)
            token = ans.token
            tokenStatus.postValue = TOKEN_GOTTEN
        }
    }

    fun login(login: String, password: String) {
        tokenStatus.value = TOKEN_GETTING
        trySendRequest {
            val ans = Model.server.login(login, password).await()
            val code =
                if (ans.errorCode == RequestCodes.INCORRECT_LOGIN_OR_PASSWORD.code) INCORRECT_LOGIN_OR_PASSWORD else {
                    unknownError(ans)
                    token = ans.token
                    tokenStatus.postValue = TOKEN_GOTTEN
                    TOKEN_GOTTEN
                }
            tokenStatus.postValue = code
        }
    }

    private fun unknownError(ans: Answer) {
        if (ans.isError)
            throw IllegalStateException("Unknown error. Code: ${ans.errorCode}")
    }

    private fun trySendRequest(block: suspend CoroutineScope.() -> Unit) {
        GlobalScope.launch {
            try {
                block()
            } catch (e: IOException) {
                Log.e("BomjException", "", e)
                _noInternetConnection.postValue = true
            }
        }

    }

    private val EMAIL_REGEX = Regex("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
    private fun isEmail(email: CharSequence) = email.matches(EMAIL_REGEX)


    companion object {
        const val LOGIN_EMPTY = 0
        const val LOGIN_CHECKING = 1
        const val LOGIN_IS_TOO_SHORT = 2
        const val LOGIN_IS_TOO_LONG = 3
        const val LOGIN_WRONG_SYMBOLS = 4
        const val LOGIN_ALREADY_USED = 5
        const val LOGIN_AVAILABLE = 6

        const val PASSWORD_EMPTY = 0
        const val PASSWORD_IS_TOO_SHORT = 1
        const val PASSWORD_IS_TOO_LONG = 2
        const val PASSWORD_AVAILABLE = 3

        const val EMAIL_EMPTY = 0
        const val EMAIL_CHECKING = 1
        const val EMAIL_WRONG = 2
        const val EMAIL_ALREADY_USED = 3
        const val EMAIL_AVAILABLE = 4

        const val TOKEN_EMPTY = 0
        const val TOKEN_GETTING = 1
        const val TOKEN_GOTTEN = 2
        const val INCORRECT_LOGIN_OR_PASSWORD = 3
    }
}

