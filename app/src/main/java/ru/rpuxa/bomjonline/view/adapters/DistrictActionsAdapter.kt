package ru.rpuxa.bomjonline.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.district_action.view.*
import org.jetbrains.anko.toast
import ru.rpuxa.bomjonline.R
import ru.rpuxa.bomjonline.getViewModel
import ru.rpuxa.bomjonline.viewmodel.ProfileViewModel
import ru.rpuxa.core.DistrictAction
import ru.rpuxa.core.UserData.Companion.DIRECT_ACTION_ALREADY_FINISHED
import ru.rpuxa.core.UserData.Companion.NOT_ENOUGH_ENERGY
import ru.rpuxa.core.UserData.Companion.NO_ERROR
import ru.rpuxa.core.error

class DistrictActionsAdapter : ListAdapter<DistrictAction, DistrictActionsAdapter.DirectActionViewHolder>(Diff) {
    class DirectActionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val energy: TextView = view.direct_action_energy
        val rubles: TextView = view.direct_action_rubles
        val authority: TextView = view.direct_action_authority
        val progress: TextView = view.direct_action_progress
        val name: TextView = view.direct_action_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectActionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.district_action, parent, false)
        return DirectActionViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DirectActionViewHolder, position: Int) {
        val activity = holder.view.context as FragmentActivity
        getItem(position).run {
            val viewModel = activity.getViewModel<ProfileViewModel>()
            fun updateProgress() {
                val progress = viewModel.getDistrictActionProgress(this)
                holder.progress.text = if (progress == count) "Выполнено!" else "$progress / $count"
            }

            holder.authority.text = "+$authorityAdd"
            holder.energy.text = "-$energyRemove"
            holder.name.text = name
            holder.rubles.text = "+$rublesAdd"

            holder.view.setOnClickListener {
                val text = when (viewModel.makeAction(this)) {
                    NO_ERROR -> "Выполнено"
                    DIRECT_ACTION_ALREADY_FINISHED -> "Действие уже закончено"
                    NOT_ENOUGH_ENERGY -> "Не хватает энергии"
                    else -> error()
                }

                activity.toast(text)
                updateProgress()
            }
            updateProgress()
        }
    }

    object Diff : DiffUtil.ItemCallback<DistrictAction?>() {
        override fun areItemsTheSame(oldItem: DistrictAction, newItem: DistrictAction): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DistrictAction, newItem: DistrictAction): Boolean =
            oldItem.id == newItem.id
    }
}