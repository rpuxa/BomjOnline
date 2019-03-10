package ru.rpuxa.bomjonline.model

import android.content.Context
import ru.rpuxa.core.GameData
import ru.rpuxa.core.UserData

class Model(appContext: Context) {
    val server: Server = Server.create()

    val dataBase = DataBase.create(appContext)

    private lateinit var _gameData: GameData
    var gameData: GameData
        get() = _gameData
        set(value) {
            _gameData = value
            dataBase.updateDirectActions(value.directActions)
        }
    lateinit var profile: UserData

    fun loadGameData() {
        val directActions = dataBase.getAllDirectActions()

        _gameData = GameData(directActions)
    }
}