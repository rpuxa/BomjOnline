package ru.rpuxa.bomjonline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.rpuxa.bomjonline.MutableLiveData
import ru.rpuxa.bomjonline.await
import ru.rpuxa.bomjonline.model
import ru.rpuxa.bomjonline.postValue
import ru.rpuxa.core.RequestCodes
import java.io.IOException

class UpdateViewModel : ViewModel() {

    private val _loadingState = MutableLiveData(PREPARING)
    val loadingState: LiveData<Int> get() = _loadingState

    private val _noInternetConnection = MutableLiveData(false)
    val noInternetConnection: LiveData<Boolean> get() = _noInternetConnection

    private val _error = MutableLiveData(RequestCodes.NO_ERROR.code)
    val error: LiveData<Int> get() = _error

    init {
        start()
    }

    private fun start() {
        _loadingState.value = PREPARING
        GlobalScope.launch {
            try {
                model.loadGameData()
                _loadingState.postValue = CHECKING_UPDATE
                val updateRequired = model.server.checkUpdate(model.gameData.hash()).await()
                if (updateRequired.required == 1) {
                    _loadingState.postValue = LOADING_UPDATE
                    model.updateGameData(
                        model.server.update().await()
                    )
                }
                _loadingState.postValue = LOADING_PROFILE
                val token = model.dataBase.loadToken()!!
                val answer = model.server.getUserData(token).await()
                if (answer.isError) {
                    _error.postValue = answer.errorCode
                    return@launch
                }
                model.profile = answer.user
                _loadingState.postValue = COMPLETE
            } catch (e: IOException) {
                e.printStackTrace()
                _noInternetConnection.postValue = true
            }
        }
    }

    fun tryAgain() {
        _noInternetConnection.postValue = false
        start()
    }

    companion object {
        const val PREPARING = 0
        const val CHECKING_UPDATE = 1
        const val LOADING_UPDATE = 2
        const val LOADING_PROFILE = 3
        const val COMPLETE = 4

        const val LAST_STATE = COMPLETE
    }
}