package ru.rpuxa.bomjonline.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding.widget.RxAdapterView
import kotlinx.android.synthetic.main.district_actions.view.*
import org.jetbrains.anko.support.v4.act
import ru.rpuxa.bomjonline.R
import ru.rpuxa.bomjonline.getViewModel
import ru.rpuxa.bomjonline.observe
import ru.rpuxa.bomjonline.view.adapters.DistrictActionsAdapter
import ru.rpuxa.bomjonline.view.adapters.DistrictSpinnerAdapter
import ru.rpuxa.bomjonline.viewmodel.DistrictActionsViewModel
import ru.rpuxa.bomjonline.viewmodel.ProfileViewModel

class DistrictActionsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.district_actions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val profileViewModel = act.getViewModel<ProfileViewModel>()
        val districtViewModel = act.getViewModel<DistrictActionsViewModel>()
        view.direct_actions_spinner.apply {
            val adapter = DistrictSpinnerAdapter(
                this@DistrictActionsFragment,
                profileViewModel,
                profileViewModel.gameData.districts
            )
            this.adapter = adapter
            RxAdapterView.itemSelections(this)
                .subscribe(districtViewModel.selectedDirectId::postValue)
        }

        val districtActionsAdapter = DistrictActionsAdapter()
        view.direct_actions_recycler.adapter = districtActionsAdapter
        view.direct_actions_recycler.layoutManager = LinearLayoutManager(context)
        view.direct_actions_recycler.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        districtViewModel.selectedDirectId.observe(this) { districtId ->
            val filter = profileViewModel.gameData.districtsActions
                .filter { it.districtId == districtId }
            districtActionsAdapter.submitList(filter)

            val district = profileViewModel.gameData.districts.find { it.id == districtId }!!
            view.direct_actions_complete_rubles.text = district.completeRubles.toString()
            view.direct_actions_complete_authority.text = district.completeAuthority.toString()
        }
    }
}