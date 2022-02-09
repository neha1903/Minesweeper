package com.example.minesweeper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.minesweeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val defaultBoard = "DEFAULT_BOARD"
        const val customBoard = "CUSTOM_BOARD"
        const val gameTypeEasy = "GAME_TYPE_EASY"
        const val gameTypeMedium = "GAME_TYPE_MEDIUM"
        const val gameTypeHard = "GAME_TYPE_MEDIUM"
        const val rows = "ROWS"
        const val column = "COLUMN"
        const val mines = "MINES"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var selectedGameType: String
    private lateinit var selectedBoardType: String

    private var show: Boolean = false
    private var numberOfRows: Int = 6
    private var numberOfColumn: Int = 6
    private var numberOfMines: Int = 6

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
            numberOfRows = 6
            numberOfColumn = 6
            numberOfMines = 6
            radioGroup.setOnCheckedChangeListener { _, _ ->
                selectedBoardType = defaultBoard
                customBoardEditTextGroup.visibility = View.GONE
                show = false
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

            val selectedOption: Int = binding.radioGroup.checkedRadioButtonId
            val radioButton: RadioButton = findViewById(selectedOption)
            when (radioButton.text) {
                getString(R.string.easy) -> {
                    selectedGameType = gameTypeEasy
                    numberOfRows = 6
                    numberOfColumn = 6
                    numberOfMines = 6
                }
                getString(R.string.medium) -> {
                    selectedGameType = gameTypeMedium
                    numberOfRows = 10
                    numberOfColumn = 10
                    numberOfMines = 10
                }
                getString(R.string.difficult) -> {
                    selectedGameType = gameTypeHard
                    numberOfRows = 14
                    numberOfColumn = 14
                    numberOfMines = 14
                }
            }
            if (selectedGameType.length < 2) {
                Toast.makeText(
                    this@MainActivity,
                    "Please select Difficulty Level to Proceed",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

        } else if (selectedBoardType == customBoard) {
            val rows = binding.editTextTextNumberOfRows.text.toString()
            val column = binding.editTextTextNumberOfColumns.text.toString()
            val mines = binding.editTextTextNumberOfMines.text.toString()
            if (rows.isEmpty() && column.isEmpty() && mines.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Please Enter Rows, Column and Mines greater or equal to 6 ",
                    Toast.LENGTH_SHORT
                ).show()
                return
            } else {
                numberOfRows = Integer.parseInt(rows)
                numberOfColumn = Integer.parseInt(column)
                numberOfMines = Integer.parseInt(mines)

                if (numberOfRows < 6 || numberOfColumn < 6 || numberOfMines < 6) {
                    Toast.makeText(
                        this@MainActivity,
                        "Please Enter Rows, Column and Mines greater or equal to 6 ",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
            }
        }


        val intent: Intent = Intent(this@MainActivity, GameActivity::class.java).apply {
            putExtra(rows, numberOfRows)
            putExtra(column, numberOfColumn)
            putExtra(mines, numberOfMines)
        }
        startActivity(intent)
    }
}