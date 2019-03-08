package ru.rpuxa.bomjonline.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.no_internet_connection.*
import ru.rpuxa.bomjonline.R

abstract class NoConnectionDialog : DialogFragment() {

    init {
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.no_internet_connection, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        no_internet_dialog_retry.setOnClickListener {
            onRetry()
            dismiss()
        }
    }

    abstract fun onRetry()
}