package ru.rpuxa.bomjonline.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import ru.rpuxa.bomjonline.R

class DirectActionsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.direct_actions, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        view.direct_actions_spinner.adapter = ArrayAdapter<Int>(this, )

    }

    class DistrictSpinnerAdapter : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        }

        override fun getItem(position: Int): Any {
        }

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getCount(): Int {

        }
    }
}