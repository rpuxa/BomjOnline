package ru.rpuxa.bomjonline.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.main.spinner_district_item.view.*
import ru.rpuxa.bomjonline.R
import ru.rpuxa.bomjonline.observe
import ru.rpuxa.bomjonline.viewmodel.ProfileViewModel
import ru.rpuxa.core.District

class DistrictSpinnerAdapter(
    private val owner: LifecycleOwner,
    private val viewModel: ProfileViewModel,
    private val districts: List<District>
) : BaseAdapter() {

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView ?: LayoutInflater.from(parent.context).inflate(R.layout.spinner_district_item, parent, false)
        val district = getItem(position)
        val icon = view.spinner_district_item_icon
        val lvl = view.spinner_district_item_lvl
        val name = view.spinner_district_item_name
        viewModel.level.observe(owner) { level ->
            val firstClosedDistrict = viewModel.gameData.districts.firstOrNull { it.lvlNeeded > level }
            val visible = level < district.lvlNeeded
            val visibility = if (visible) View.VISIBLE else View.INVISIBLE
            icon.visibility = visibility
            lvl.visibility = visibility
            name.text = if (!visible || district.id == firstClosedDistrict?.id) district.name else "Недоступно"
        }
        lvl.text = "${district.lvlNeeded} ур."

        return view
    }

    override fun getItem(position: Int): District = districts[position]

    override fun getItemId(position: Int): Long = getItem(position).id.toLong()

    override fun getCount(): Int = districts.size
}