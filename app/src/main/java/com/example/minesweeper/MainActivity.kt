package com.example.minesweeper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.minesweeper.databinding.ActivityMainBinding

const val boardType = "BOARD_TYPE"
const val defaultBoard = "DEFAULT_BOARD"
const val customBoard = "CUSTOM_BOARD"
const val gameType = "GAME_TYPE"
const val gameTypeEasy = "GAME_TYPE_EASY"
const val gameTypeMedium = "GAME_TYPE_MEDIUM"
const val gameTypeHard = "GAME_TYPE_MEDIUM"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var selectedGameType: String
    private lateinit var selectedBoardType: String

    private var show: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        selectedBoardType = defaultBoard
        selectedGameType = gameTypeEasy

        binding.apply {
            radioButtonEasy.isChecked = true
            customBoardEditTextGroup.visibility = View.GONE
            radioGroup.setOnCheckedChangeListener { _, _ ->
                selectedBoardType = defaultBoard
                customBoardEditTextGroup.visibility = View.GONE
            }
            customBoardButton.setOnClickListener {
                setOnCustomBoardClickListener()
            }
            gameStartButton.setOnClickListener {
                setOnStartGameClick()
            }
        }
    }


    private fun setOnCustomBoardClickListener() {
        binding.apply {
            radioGroup.clearCheck()
            selectedGameType = ""
            if (show) {
                customBoardEditTextGroup.visibility = View.GONE
                selectedBoardType = defaultBoard
                show = false
            } else {
                customBoardEditTextGroup.visibility = View.VISIBLE
                selectedBoardType = customBoard
                show = true
            }
        }
    }

    private fun setOnStartGameClick() {

        if (selectedBoardType == defaultBoard) {
            if (selectedGameType.length < 2) {
                Toast.makeText(
                    this@MainActivity,
                    "Please select Difficulty Level to Proceed",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        } else if (selectedBoardType == customBoard) {
            // check that user has number of rows and Column and before proceeding to start
            // for now lets Return so that the application will not crash
            Toast.makeText(
                this@MainActivity,
                "Custom Board is Under Construction",
                Toast.LENGTH_SHORT
            )
                .show()
            return
        }

        val selectedOption: Int = binding.radioGroup.checkedRadioButtonId
        val radioButton: RadioButton = findViewById(selectedOption)
        when (radioButton.text) {
            getString(R.string.easy) -> {
                selectedGameType = gameTypeEasy
            }
            getString(R.string.medium) -> {
                selectedGameType = gameTypeMedium
            }
            getString(R.string.difficult) -> {
                selectedGameType = gameTypeHard
            }
        }

        val intent: Intent = Intent(this@MainActivity, GameActivity::class.java).apply {
            putExtra(boardType, selectedBoardType)
            putExtra(gameType, selectedGameType)
        }
        startActivity(intent)
    }
}