package ru.rpuxa.bomjonline.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_update.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.act
import ru.rpuxa.bomjonline.R
import ru.rpuxa.bomjonline.getViewModel
import ru.rpuxa.bomjonline.observe
import ru.rpuxa.bomjonline.view.NoConnectionDialog
import ru.rpuxa.bomjonline.viewmodel.UpdateViewModel
import ru.rpuxa.bomjonline.viewmodel.UpdateViewModel.Companion.CHECKING_UPDATE
import ru.rpuxa.bomjonline.viewmodel.UpdateViewModel.Companion.COMPLETE
import ru.rpuxa.bomjonline.viewmodel.UpdateViewModel.Companion.LOADING_PROFILE
import ru.rpuxa.bomjonline.viewmodel.UpdateViewModel.Companion.LOADING_UPDATE
import ru.rpuxa.bomjonline.viewmodel.UpdateViewModel.Companion.PREPARING
import ru.rpuxa.core.error

class UpdateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        val viewModel = getViewModel<UpdateViewModel>()

        val step = 100 / UpdateViewModel.LAST_STATE

        viewModel.loadingState.observe(this) { state ->
            loading_progress_bar.progress = step * state
            val text = when (state) {
                PREPARING -> "Подготовка..."
                CHECKING_UPDATE -> "Проверка обновлений..."
                LOADING_UPDATE -> "Загрузка обновления..."
                LOADING_PROFILE -> "Загрузка профиля..."
                COMPLETE -> {
                    onUpdated()
                    return@observe
                }
                else -> error()
            }
            loading_text.text = text
        }

        viewModel.noInternetConnection.observe(this) {
            if (it) {
                NCDialog().show(supportFragmentManager, "")
            }
        }

        viewModel.error.observe(this) {
            if (it != 0)
                startActivity<ErrorActivity>(ErrorActivity.ERROR_CODE to it)
        }
    }

    override fun onBackPressed() {
    }

    private fun onUpdated() {
        startActivity<MainActivity>()
    }

    class NCDialog : NoConnectionDialog() {
        override fun onRetry() {
            act.getViewModel<UpdateViewModel>().tryAgain()
        }
    }
}
