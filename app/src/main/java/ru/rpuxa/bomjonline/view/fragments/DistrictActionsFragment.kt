package ru.rpuxa.bomjonline.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding.widget.RxAdapterView
import kotlinx.android.synthetic.main.direct_actions.view.*
import kotlinx.android.synthetic.main.spinner_direct_item.view.*
import org.jetbrains.anko.support.v4.act
import ru.rpuxa.bomjonline.R
import ru.rpuxa.bomjonline.getViewModel
import ru.rpuxa.bomjonline.observe
import ru.rpuxa.bomjonline.postValue
import ru.rpuxa.bomjonline.view.adapters.DistrictActionsAdapter
import ru.rpuxa.bomjonline.viewmodel.DistrictActionsViewModel
import ru.rpuxa.bomjonline.viewmodel.ProfileViewModel
import ru.rpuxa.core.District

class DistrictActionsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.direct_actions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val profileViewModel = act.getViewModel<ProfileViewModel>()
        val districtViewModel = act.getViewModel<DistrictActionsViewModel>()
        view.direct_actions_spinner.apply {
            val adapter = DistrictSpinnerAdapter(profileViewModel.gameData.districts)
            this.adapter = adapter
            RxAdapterView.itemClicks(this)
                .subscribe(districtViewModel.selectedDirectId::postValue)
        }

        val districtActionsAdapter = DistrictActionsAdapter()
        view.direct_actions_recycler.adapter = districtActionsAdapter

        districtViewModel.selectedDirectId.observe(this) {
            districtActionsAdapter.submitList()
        }
    }

    class DistrictSpinnerAdapter(private val districts: List<District>) : BaseAdapter() {

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            if (convertView != null)
                return convertView
            val view = LayoutInflater.from(parent.context).inflate(R.layout.spinner_direct_item, parent, false)
            view.spinner_direct_item_name.text = getItem(position).name

            return view
        }

        override fun getItem(position: Int): District = districts[position]

        override fun getItemId(position: Int): Long = getItem(position).id.toLong()

        override fun getCount(): Int = districts.size
    }
}