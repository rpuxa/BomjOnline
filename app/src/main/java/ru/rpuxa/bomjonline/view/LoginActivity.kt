package ru.rpuxa.bomjonline.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.login_activity.*
import ru.rpuxa.bomjonline.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        var isUserLogging = true

        RxTextView.beforeTextChangeEvents()
    }

    private fun setUserLoggin(bFlag: Boolean) {
        login_text.textSize = if (bFlag)
    }

    companion object {

        private fun setViewActivated(view: TextView, bFlag: Boolean) {
            view.textSize = if (bFlag) 18f else 13f
        }
    }
}