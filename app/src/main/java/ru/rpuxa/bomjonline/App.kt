package ru.rpuxa.bomjonline

import android.app.Application
import ru.rpuxa.bomjonline.model.Model

lateinit var model: Model
    private set

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        model = Model(this)
    }
}