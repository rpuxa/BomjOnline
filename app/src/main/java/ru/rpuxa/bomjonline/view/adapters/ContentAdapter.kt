package ru.rpuxa.bomjonline.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.rpuxa.bomjonline.view.fragments.DistrictActionsFragment

class ContentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    val fragments = arrayOf(
        DistrictActionsFragment()
    )

    override fun getItem(position: Int): Fragment =
            fragments[position]

    /**
     * Return the number of views available.
     */
    override fun getCount(): Int = fragments.size
}