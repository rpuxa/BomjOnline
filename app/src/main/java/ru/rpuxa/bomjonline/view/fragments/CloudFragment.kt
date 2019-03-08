package ru.rpuxa.bomjonline.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.cloud_fragment.view.*
import ru.rpuxa.bomjonline.R

class CloudFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.cloud_fragment, container, false)

    fun setConfig(config: Config) {
        val view = view as? CardView ?: return
        view.clound_text.text = config.text
        view.cloud_icon.setImageResource(config.icon)
        view.setCardBackgroundColor(resources.getColor(config.background))
    }

    class Config {
        var text = "text"
        @DrawableRes
        var icon = 0
        @ColorInt
        var background = 0

        companion object {
            inline fun create(builder: Config.() -> Unit): Config {
                val config = Config()
                builder(config)
                return config
            }
        }
    }

    companion object Configs {

        fun error(text: String) = Config.create {
            this.text = text
            icon = R.drawable.error
            background = R.color.cloud_error_background
        }

        fun accept(text: String) = Config.create {
            this.text = text
            icon = R.drawable.accept
            background = R.color.cloud_accept_background
        }

        fun waiting(text: String) = Config.create {
            this.text = text
            icon = R.drawable.clock
            background = R.color.cloud_wait_background
        }
    }
}