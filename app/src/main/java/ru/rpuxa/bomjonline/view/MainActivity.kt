package ru.rpuxa.bomjonline.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.rpuxa.bomjonline.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onBackPressed() {
    }
}