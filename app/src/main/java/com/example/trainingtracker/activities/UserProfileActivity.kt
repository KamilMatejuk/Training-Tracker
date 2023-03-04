package com.example.trainingtracker.activities

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.trainingtracker.R
import com.example.trainingtracker.databinding.ActivityUserProfileBinding
import com.example.trainingtracker.dbconnection.Room
import com.example.trainingtracker.dbconnection.items.Sex
import com.example.trainingtracker.dbconnection.items.UserItem
import com.example.trainingtracker.fragments.SwitchMeasureType
import com.example.trainingtracker.fragments.SwitchOptionsFragment
import com.example.trainingtracker.fragments.SwitchSex
import com.example.trainingtracker.fragments.SwitchWeightType

class UserProfileActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityUserProfileBinding

    private var user: UserItem? = null
    private var sex: SwitchSex = SwitchSex.MALE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            val fragmentSwitchSex = SwitchOptionsFragment.newInstance("tagSex", enumValues<SwitchSex>())
            fragmentTransaction.replace(R.id.switch_sex, fragmentSwitchSex, "tagSex")
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit()

            fragmentManager.setFragmentResultListener("tagSex", fragmentSwitchSex) { _, bundle ->
                sex = bundle.getSerializable("key") as SwitchSex
            }
        }
        getUser()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun getUser() {
        Thread {
            run {
                user = Room.getUser()
                if (user == null) return@run
                val sexFragment = supportFragmentManager.findFragmentByTag("tagSex") as SwitchOptionsFragment<SwitchSex>

                sexFragment.switchBtnOn(this, when (user!!.sex) { Sex.MALE -> 0; Sex.FEMALE -> 1})

            }
        }
    }
}