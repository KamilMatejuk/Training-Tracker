package com.example.trainingtracker.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentTransaction
import com.example.trainingtracker.R
import com.example.trainingtracker.Tools
import com.example.trainingtracker.databinding.ActivityUserProfileBinding
import com.example.trainingtracker.dbconnection.FileManager
import com.example.trainingtracker.dbconnection.Room
import com.example.trainingtracker.dbconnection.items.Sex
import com.example.trainingtracker.dbconnection.items.UserItem
import com.example.trainingtracker.fragments.OptionSex
import com.example.trainingtracker.fragments.SwitchOptionsFragment
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class UserProfileActivity : ThemeChangingActivity() {
    private lateinit var binding: ActivityUserProfileBinding

    private var user: UserItem? = null
    private var sex: Sex = Sex.MALE

    private val CREATE_FILE = 1
    private val OPEN_FILE = 2



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
                Room.updateHeight(user!!.id!!, (binding.height.text.toString().toFloatOrNull() ?: 0f) / 100)
                runOnUiThread {
                    Toast.makeText(this, "Zapisano", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }.start()

        resources.openRawResource(R.raw.default_fabrykasily)
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
                runOnUiThread {
                    binding.username.setText(user?.username)
                    binding.height.setText((user?.height?.times(100)?.toInt()).toString())
                    binding.weight.text = user?.weight?.map { (date, weight) -> "$weight on $date" }
                        ?.joinToString("\n")
                }
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun addWeight() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.Popup)
        builder.setTitle("Waga w kg")
        val input = EditText(this)
        input.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
        input.gravity = Gravity.CENTER
        input.setTextColor(Tools.colorFromAttr(this, R.attr.myForegroundColor))
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            val weight = input.text.toString().toFloatOrNull()
            if (weight == null) {
                Toast.makeText(this, "Bląd odczytu wagi: ${input.text}", Toast.LENGTH_SHORT).show()
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

    fun reload() {
        runOnUiThread {
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    fun clearData(view: View) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.Popup)
        builder.setTitle("Jakie dane chcesz usunąć?")

        builder.setItems(arrayOf("Dane użytkownika", "Dane ćwiczeń", "Plany treningowe", "Historia treningów", "Anuluj")
        ) { dialog, option ->
            when (option) {
                0 -> Thread { run { Room.clearUser(); reload() } }.start()
                1 -> Thread { run { Room.clearExercise() } }.start()
                2 -> Thread { run {  } }.start()
                3 -> Thread { run { Room.clearHistory() } }.start()
                4 -> dialog.cancel()
            }
        }
        builder.show()
    }

    fun exportData(view: View) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/csv"
            val date = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now())
            putExtra(Intent.EXTRA_TITLE, "dane ${date}.csv")
        }
        startActivityForResult(intent, CREATE_FILE)
    }

    fun importData(view: View) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, OPEN_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        Log.d("ACT RES", "$requestCode $resultCode $resultData")
        if (resultCode != Activity.RESULT_OK) return
        Thread {
            run {
                Log.d("ACT RES OK", resultData.toString())
                resultData?.data?.also { uri ->
                    when (requestCode) {
                        OPEN_FILE -> FileManager.load(this, uri)
                        CREATE_FILE -> FileManager.save(this, uri)
                    }
                }
            }
        }.start()
    }

}