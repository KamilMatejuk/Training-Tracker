package com.example.trainingtracker.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.trainingtracker.R
import com.example.trainingtracker.Tools
import com.example.trainingtracker.databinding.ActivityUserProfileBinding
import com.example.trainingtracker.dbconnection.Room
import com.example.trainingtracker.dbconnection.items.Sex
import com.example.trainingtracker.dbconnection.items.UserItem
import com.example.trainingtracker.fragments.SwitchOptionsFragment
import com.example.trainingtracker.fragments.OptionSex
import java.time.LocalDate
import java.util.*

class UserProfileActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityUserProfileBinding

    private var user: UserItem? = null
    private var sex: Sex = Sex.MALE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            val fragmentSwitchSex = SwitchOptionsFragment.newInstance("tagSex", enumValues<OptionSex>())
            fragmentTransaction.replace(R.id.switch_sex, fragmentSwitchSex, "tagSex")
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            supportFragmentManager.setFragmentResultListener("tagSex", fragmentSwitchSex) { _, bundle ->
                sex = when (bundle.getSerializable("key") as OptionSex) {
                    OptionSex.MALE -> Sex.MALE
                    OptionSex.FEMALE -> Sex.FEMALE
                }
            }
        }
        getUser()
        binding.add.setOnClickListener { addWeight() }
    }

    override fun onBackPressed() {
        Thread {
            run {
                Room.updateUsername(user!!.id!!, binding.username.text.toString())
                Room.updateSex(user!!.id!!, sex)
                Room.updateHeight(user!!.id!!, binding.height.text.toString().toFloatOrNull() ?: 0f)
                runOnUiThread {
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun getUser() {
        supportFragmentManager.executePendingTransactions()
        Thread {
            run {
                user = Room.getUser()
                if (user == null) {
                    user = UserItem(1, "", Sex.MALE, 0f, hashMapOf())
                    Room.addUser(user!!)
                }
                sex = user!!.sex
                val sexFragment = supportFragmentManager.findFragmentByTag("tagSex")
                if (sexFragment != null) {
                    (sexFragment as SwitchOptionsFragment<OptionSex>).switchBtnOn(this,
                        when (user!!.sex) { Sex.MALE -> 0; Sex.FEMALE -> 1 })
                }
                binding.username.setText(user?.username)
                binding.height.setText(user?.height.toString())
                binding.weight.text = user?.weight?.map { (date, weight) -> "$weight on $date" }?.joinToString("\n")
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun addWeight() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.Popup)
        builder.setTitle("Weight in kg")
        val input = EditText(this)
        input.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
        input.gravity = Gravity.CENTER
        input.setTextColor(Tools.colorFromAttr(this, R.attr.myForegroundColor))
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            val weight = input.text.toString().toFloatOrNull()
            if (weight == null) {
                Toast.makeText(this, "Couldn't parse ${input.text}", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            user!!.weight[LocalDate.now()] = weight
            binding.weight.text = user?.weight?.map { (date, weight) -> "$weight on $date" }?.joinToString("\n")
            Thread {
                run {
                    Room.updateWeight(user!!.id!!, user!!.weight)
                }
            }.start()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

}