package ru.rpuxa.bomjonline.view.activities

import android.os.Bundle
import android.os.Process
import android.os.Process.killProcess
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_error.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import ru.rpuxa.bomjonline.R
import ru.rpuxa.bomjonline.model
import ru.rpuxa.core.RequestCodes

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)

        val code = intent?.extras?.getInt(ERROR_CODE)!!

        val buttonText: String
        val listener: (View) -> Unit
        val comment: String
        when (code) {
            RequestCodes.UNKNOWN_TOKEN.code -> {
                buttonText = "Войти заново"
                comment = "Неверный токен. Попробуйте перезойти"
                listener = {
                    GlobalScope.launch {
                        model.dataBase.saveToken(null)
                        runOnUiThread {
                            startActivity<LoginActivity>()
                        }
                    }
                }
            }
            RequestCodes.OUT_OF_SYNC.code -> {
                buttonText = "Перезайти"
                comment = "Произошла рассинхронизация с сервером"
                listener = { TODO("Когда будет splash activity  вызвать его тут") }
            }
            else -> {
                buttonText = "Выйти из игры"
                comment = "Неизвестная ошибка. Попробуйте перезойти в игру"
                listener = { killProcess(Process.myPid()) }
            }
        }

        error_code.text = code.toString()
        error_comment.text = comment
        error_fix_button.text = buttonText
        error_fix_button.setOnClickListener(listener)
    }

    override fun onBackPressed() {
    }

    companion object {
        const val ERROR_CODE = "e"
    }
}
