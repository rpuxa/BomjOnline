package ru.rpuxa.bomjonline.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.status.*
import org.jetbrains.anko.support.v4.act
import ru.rpuxa.bomjonline.R
import ru.rpuxa.bomjonline.getViewModel
import ru.rpuxa.bomjonline.observe
import ru.rpuxa.bomjonline.toObservable
import ru.rpuxa.bomjonline.viewmodel.ProfileViewModel
import rx.Observable
import rx.android.schedulers.AndroidSchedulers

class StatusFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.status, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val profileViewModel = act.getViewModel<ProfileViewModel>()

        Observable.combineLatest(
            profileViewModel.energy.toObservable(this),
            profileViewModel.maxEnergy.toObservable(this)
        ) { energy, maxEnergy -> "$energy / $maxEnergy" }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(status_energy::setText)

        Observable.combineLatest(
            profileViewModel.health.toObservable(this),
            profileViewModel.maxHealth.toObservable(this)
        ) { health, maxHealth -> "$health / $maxHealth" }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(status_health::setText)

        profileViewModel.rubles.observe(this) {
            status_rubles.text = it.toString()
        }

        Observable.combineLatest(
            profileViewModel.login.toObservable(this),
            profileViewModel.level.toObservable(this)
        ) { login, level -> "$login ($level уровень)" }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(status_name::setText)
    }
}