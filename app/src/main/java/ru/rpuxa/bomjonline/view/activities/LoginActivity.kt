package ru.rpuxa.bomjonline.view.activities

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.login_activity.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.act
import ru.rpuxa.bomjonline.*
import ru.rpuxa.bomjonline.view.NoConnectionDialog
import ru.rpuxa.bomjonline.view.fragments.CloudFragment
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.EMAIL_ALREADY_USED
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.EMAIL_AVAILABLE
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.EMAIL_CHECKING
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.EMAIL_EMPTY
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.EMAIL_WRONG
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.INCORRECT_LOGIN_OR_PASSWORD
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.LOGIN_ALREADY_USED
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.LOGIN_AVAILABLE
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.LOGIN_CHECKING
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.LOGIN_EMPTY
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.LOGIN_IS_TOO_LONG
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.LOGIN_IS_TOO_SHORT
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.LOGIN_WRONG_SYMBOLS
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.PASSWORD_AVAILABLE
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.PASSWORD_EMPTY
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.PASSWORD_IS_TOO_LONG
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.PASSWORD_IS_TOO_SHORT
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.TOKEN_EMPTY
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.TOKEN_GETTING
import ru.rpuxa.bomjonline.viewmodel.LoginViewModel.Companion.TOKEN_GOTTEN
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private var isUserLogging = MutableLiveData(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val viewModel = getViewModel<LoginViewModel>()

        if (isUserLogging.nnValue)
            prepareForLogin()
        else
            prepareForRegister()

        login_text.setOnClickListener {
            prepareForLogin()
        }

        register_text.setOnClickListener {
            prepareForRegister()
        }

        val passwordObservable = RxTextView.textChanges(password)
            .debounce(DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
        val confirmPasswordObservable = RxTextView.textChanges(confirm_password)
        val loginObservable = RxTextView.textChanges(login)
            .debounce(DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
        val emailObservable = RxTextView.textChanges(email)
            .debounce(DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
        val isUserLoggingObservable = isUserLogging.toObservableForever()


        Observable.combineLatest(
            passwordObservable,
            isUserLoggingObservable
        ) { pass, isUserLogging ->
            if (!isUserLogging)
                viewModel.passwordToCheck.postValue = pass.toString()
        }.subscribe()

        Observable.combineLatest(
            loginObservable,
            isUserLoggingObservable
        ) { login, isUserLogging ->
            if (!isUserLogging)
                viewModel.loginToCheck.postValue = login.toString()
        }.subscribe()

        Observable.combineLatest(
            emailObservable,
            isUserLoggingObservable
        ) { email, isUserLogging ->
            if (!isUserLogging)
                viewModel.emailToCheck.postValue = email.toString()
        }.subscribe()


        val login_info_cloud = login_info_cloud as CloudFragment

        val pass_info_cloud = pass_info_cloud as CloudFragment

        val email_info_cloud = email_info_cloud as CloudFragment


        val isLoginReady =
            Observable.combineLatest(
                viewModel.loginStatus.toObservable(this),
                isUserLoggingObservable
            ) { status, isUserLogging ->
                if (isUserLogging)
                    return@combineLatest true
                login_info_cloud.view?.visibility = if (status == LOGIN_EMPTY) View.GONE else View.VISIBLE
                when (status) {
                    LOGIN_CHECKING -> {
                        login_info_cloud.setConfig(CloudFragment.waiting("Проверка логина..."))
                    }
                    LOGIN_IS_TOO_SHORT -> {
                        login_info_cloud.setConfig(
                            CloudFragment.error(
                                "Логин не может быть короче ${viewModel.regParams.value?.loginMinLength} символов"
                            )
                        )
                    }
                    LOGIN_IS_TOO_LONG -> {
                        login_info_cloud.setConfig(
                            CloudFragment.error(
                                "Логин не может быть длиннее ${viewModel.regParams.value?.loginMaxLength} символов"
                            )
                        )
                    }
                    LOGIN_WRONG_SYMBOLS -> {
                        login_info_cloud.setConfig(
                            CloudFragment.error(
                                "Логин должен содержать только данные символы: " + viewModel.regParams.value?.loginSymbols
                            )
                        )
                    }
                    LOGIN_ALREADY_USED -> {
                        login_info_cloud.setConfig(
                            CloudFragment.error(
                                "Логин занят, попробуйте другой"
                            )
                        )
                    }
                    LOGIN_AVAILABLE -> {
                        login_info_cloud.setConfig(
                            CloudFragment.accept("Логин подходит!")
                        )
                        return@combineLatest true
                    }
                }
                false
            }


        val isPasswordReady =
            Observable.combineLatest(
                viewModel.passwordStatus.toObservable(this),
                confirmPasswordObservable,
                isUserLoggingObservable
            ) { status, confirmPassword, isUserLogging ->
                if (isUserLogging)
                    return@combineLatest true
                pass_info_cloud.view?.visibility = if (status == PASSWORD_EMPTY) View.GONE else View.VISIBLE
                when (status) {
                    PASSWORD_IS_TOO_SHORT -> pass_info_cloud.setConfig(
                        CloudFragment.error("Пароль должен иметь не менее ${viewModel.regParams.value?.passwordMinLength} символов")
                    )
                    PASSWORD_IS_TOO_LONG -> pass_info_cloud.setConfig(
                        CloudFragment.error("Пароль должен иметь не более ${viewModel.regParams.value?.passwordMaxLength} символов")
                    )
                    PASSWORD_AVAILABLE -> {
                        if (confirmPassword.toString() == password.text.toString()) {
                            pass_info_cloud.setConfig(
                                CloudFragment.accept("Пароли совпадают!")
                            )
                            return@combineLatest true
                        } else {
                            pass_info_cloud.setConfig(
                                CloudFragment.error("Пароли не совпадают!")
                            )
                        }
                    }
                }
                false
            }

        val isEmailReady =
            Observable.combineLatest(
                viewModel.emailStatus.toObservable(this),
                isUserLoggingObservable
            ) { status, isUserLogging ->
                if (isUserLogging)
                    return@combineLatest true
                email_info_cloud.view?.visibility = if (status == EMAIL_EMPTY) View.GONE else View.VISIBLE

                when (status) {
                    EMAIL_WRONG -> email_info_cloud.setConfig(
                        CloudFragment.error("Неверно введен e-mail")
                    )
                    EMAIL_CHECKING -> email_info_cloud.setConfig(
                        CloudFragment.waiting("Проверяем e-mail...")
                    )
                    EMAIL_ALREADY_USED -> email_info_cloud.setConfig(
                        CloudFragment.error("E-mail уже используется")
                    )
                    EMAIL_AVAILABLE -> {
                        email_info_cloud.setConfig(
                            CloudFragment.accept("E-mail введен верно!")
                        )
                        return@combineLatest true
                    }
                }

                false
            }

        Observable.combineLatest(
            isLoginReady,
            isPasswordReady,
            isEmailReady,
            loginObservable,
            passwordObservable
        ) { loginReady, passwordReady, email, login, password ->
            if (login.isBlank() || password.isBlank())
                false
            else
                loginReady && passwordReady && email
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                confirm_button.isEnabled = it
            }


        confirm_button.setOnClickListener {
            val loginString = login.text.toString()
            val passwordString = password.text.toString()
            if (isUserLogging.nnValue) {
                viewModel.login(loginString, passwordString)
            } else {
                viewModel.register(
                    loginString,
                    passwordString,
                    email.text.toString()
                )
            }
        }

        viewModel.tokenStatus.observe(this) { status ->
            when (status) {
                TOKEN_EMPTY -> logging(false)
                TOKEN_GETTING -> logging(true)
                TOKEN_GOTTEN -> successfulLogin()
                INCORRECT_LOGIN_OR_PASSWORD -> {
                    longToast("Неверный логин или пароль!")
                    viewModel.tokenStatus.value = TOKEN_EMPTY
                }
            }
        }

        viewModel.noInternetConnection.observe(this) { noConnection ->
            if (noConnection) {
                NCDialog().show(supportFragmentManager, "")
            }
        }
    }

    private val registerViews by lazy {
        arrayOf(
            confirm_password,
            email,
            login_info_cloud.view!!,
            pass_info_cloud.view!!,
            email_info_cloud.view!!
        )
    }

    private val allViews by lazy {
        arrayOf(
            login,
            password,
            confirm_button,
            *registerViews
        )
    }

    private fun prepareForLogin() {
        login_text.pickOut(true)
        register_text.pickOut(false)
        setVisibility(View.GONE, *registerViews)
        isUserLogging.value = true
    }

    private fun prepareForRegister() {
        login_text.pickOut(false)
        register_text.pickOut(true)
        setVisibility(View.VISIBLE, *registerViews)
        isUserLogging.value = false
    }

    private fun TextView.pickOut(bFlag: Boolean) {
        textSize = if (bFlag) 18f else 14f
        setTypeface(null, if (bFlag) Typeface.BOLD else Typeface.NORMAL)
    }

    private fun logging(bFlag: Boolean) {
        for (view in allViews)
            view.isEnabled = !bFlag
        login_progress_bar.visibility = if (bFlag) View.VISIBLE else View.GONE
    }

    private fun successfulLogin() {
        startActivity<UpdateActivity>()
    }

    override fun onBackPressed() {
    }

    companion object {
        const val DEBOUNCE_TIME: Long = 500

    }

    class NCDialog : NoConnectionDialog() {
        override fun onRetry() {
            val viewModel = act.getViewModel<LoginViewModel>()
            viewModel.tryAgain()
        }
    }
}