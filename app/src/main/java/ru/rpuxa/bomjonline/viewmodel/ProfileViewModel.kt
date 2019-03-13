package ru.rpuxa.bomjonline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.rpuxa.bomjonline.MutableLiveData
import ru.rpuxa.bomjonline.await
import ru.rpuxa.bomjonline.model
import ru.rpuxa.bomjonline.postValue
import ru.rpuxa.core.District
import ru.rpuxa.core.DistrictAction
import ru.rpuxa.core.GameData
import ru.rpuxa.core.RequestCodes
import ru.rpuxa.core.UserData.Companion.NO_ERROR

class ProfileViewModel : ViewModel() {

    private val _rubles = MutableLiveData<Long>()
    val rubles: LiveData<Long> get() = _rubles

    private val _authority = MutableLiveData<Long>()
    val authority: LiveData<Long> get() = _authority

    private val _energy = MutableLiveData<Int>()
    val energy: LiveData<Int> get() = _energy

    private val _health = MutableLiveData<Int>()
    val health: LiveData<Int> get() = _health

    private val _maxHealth = MutableLiveData<Int>()
    val maxHealth: LiveData<Int> get() = _maxHealth

    private val _maxEnergy = MutableLiveData<Int>()
    val maxEnergy: LiveData<Int> get() = _maxEnergy

    private val _error = MutableLiveData(RequestCodes.NO_ERROR.code)
    val error: LiveData<Int> get() = _error

    val gameData: GameData get() = model.gameData


    init {
        update()
    }

    fun getDirectActionProgress(districtAction: DistrictAction): Int {
        return model.profile.directActionsProgress[districtAction.id] ?: 0
    }

    private fun update() {
        model.profile.run {
            _rubles.value = rubles
            _authority.value = allAuthority
            _energy.value = energy
            _health.value = health
            _maxEnergy.value = maxEnergy
            _maxHealth.value = maxHealth
        }
    }

    fun makeAction(action: DistrictAction): Int {
        val res = model.profile.makeAction(action)
        if (res != NO_ERROR) return res
        update()

        GlobalScope.launch {
            val result = model.server.makeDirectAction(model.dataBase.loadToken()!!, action.id).await().errorCode
            if (result == RequestCodes.OUT_OF_SYNC.code ||
                result == RequestCodes.UNKNOWN_TOKEN.code
            ) {
                _error.postValue = result
            }
            if (result != RequestCodes.NO_ERROR.code)
                error(result)
        }

        return res
    }
}