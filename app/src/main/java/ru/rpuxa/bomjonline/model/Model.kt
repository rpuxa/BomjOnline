package ru.rpuxa.bomjonline.model

import android.content.Context
import ru.rpuxa.core.GameData
import ru.rpuxa.core.UserData

class Model(appContext: Context) {
    val server: Server = Server.create()

    val dataBase = DataBase.create(appContext)

    private lateinit var _gameData: GameData
    val gameData: GameData get() = _gameData

    lateinit var profile: UserData

    fun loadGameData() {
        _gameData = dataBase.loadGameData()
    }

    fun updateGameData(gameData: GameData) {
        _gameData = gameData
        dataBase.updateGameData(gameData)
    }
}