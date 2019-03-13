package ru.rpuxa.bomjonline.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.control_bar.*
import org.jetbrains.anko.startActivity
import ru.rpuxa.bomjonline.R
import ru.rpuxa.bomjonline.getViewModel
import ru.rpuxa.bomjonline.observe
import ru.rpuxa.bomjonline.view.adapters.ContentAdapter
import ru.rpuxa.bomjonline.view.fragments.DistrictActionsFragment
import ru.rpuxa.bomjonline.viewmodel.ProfileViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contentAdapter = ContentAdapter(supportFragmentManager)
        content_pager.adapter = contentAdapter

        control_bar_directs.setOnClickListener {
            val index = contentAdapter.indexOf<DistrictActionsFragment>()
            content_pager.currentItem = index
        }

        getViewModel<ProfileViewModel>().error.observe(this) {
            if (it != 0)
                startActivity<ErrorActivity>(ErrorActivity.ERROR_CODE to it)
        }
    }


    override fun onBackPressed() {
    }
}
