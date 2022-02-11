package com.example.minesweeper

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.minesweeper.databinding.ActivityMainBinding

/*Main Activity is Application Launcher Activity*/
class MainActivity : AppCompatActivity() {
    /* Companion objects are singleton objects whose properties and
    functions are tied to a class but not to the instance of that class
    — basically like the “static” keyword in Java. */
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

    /* Variable Declaration and Initialization Region Start */
    private lateinit var binding: ActivityMainBinding
    private lateinit var selectedGameType: String
    private lateinit var selectedBoardType: String

    private var show: Boolean = false
    private var numberOfRows: Int = 6
    private var numberOfColumn: Int = 6
    private var numberOfMines: Int = 6
    /* Variable Declaration and Initialization Region End */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*View binding is a feature that allows you to more easily write code that interacts with views.
        Once view binding is enabled in a module,
        it generates a binding class for each XML layout file present in that module.
        An instance of a binding class contains direct references to all views that have an ID in the corresponding layout.
        In most cases, view binding replaces findViewById.
        View binding is enabled on a module by module basis.
        */

        // Initializing binding variable This creates an Instance of Binding Class
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Get a reference to the root view by calling the GetRoot
        val view = binding.root
        // pass view to setContentView() for making the view active on Screen
        setContentView(view)

        /* setting Default values for type of Board and Game Level
        * */
        selectedBoardType = defaultBoard
        selectedGameType = gameTypeEasy

        // using Instance of the ActivityMainBinding class for calling any of the views in XML
        binding.apply {
            // Checked the radio button,
            // So That when Screen Loads Everytime the Selected Game level is Easy
            radioButtonEasy.isChecked = true
            /* Set visibility to Gone for CustomBoardEditTextGroup
            (that contain all the edit Text used for Customizing The Board) */
            customBoardEditTextGroup.visibility = View.GONE
            /*Setting Default values for easy Level*/
            numberOfRows = 6
            numberOfColumn = 6
            numberOfMines = 6
            radioGroup.setOnCheckedChangeListener { _, _ ->
                /*On Clicking any of the radioGroup hiding the Radio Group */
                selectedBoardType = defaultBoard
                customBoardEditTextGroup.visibility = View.GONE
                show = false
            }
            customBoardButton.setOnClickListener {
                // Setting On Custom Board button
                setOnCustomBoardClickListener()
            }
            gameStartButton.setOnClickListener {
                // Setting Custom Game Start Button
                setOnStartGameClick()
            }

            info.setOnClickListener {
                showInstructions()
            }

        }
    }

    private fun updateTime() {
        /*Shared pref is used for saving key-value
        * can be store private Mode*/
        // instance of Shared preference is created in private mode
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val bestTime = sharedPreferences.getInt(GameActivity.BEST_GAME_TIME, 0)
        val lastGameTime = sharedPreferences.getInt(GameActivity.LAST_GAME_TIME, 0)
        binding.bestTimeTextview.text = getString(R.string.best_time, bestTime)
        binding.lastGmeTimeTextview.text = getString(R.string.last_game_time, lastGameTime)
    }

    private fun setOnCustomBoardClickListener() {
        // using Instance of the ActivityMainBinding class for calling any of the views in XML
        binding.apply {
            radioGroup.clearCheck()
            /*Toggling the Custom Board Group View by On Clicking
            * and Setting the BoardType on the basic of Toggle*/
            selectedGameType = ""
            if (show) { // using Show var here to check the Group visibility for toggle
                /*if the visibility is true then  */
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
        /*Before Starting the Game Checking the Parameters as Required on the basic of type of Board Selected.*/
        if (selectedBoardType == defaultBoard) { // Default board has 3 Level Of Difficulty
            /*using radioGroup for capturing radio Button ID that is checked in the Group */
            val selectedOption: Int = binding.radioGroup.checkedRadioButtonId
            // using Instance of the ActivityMainBinding class for calling any of the views in XML
            /*Finding radio Button by findViewById in Activity*/
            val radioButton: RadioButton = findViewById(selectedOption)
            when (radioButton.text) {
                getString(R.string.easy) -> {
                    /*grid Matrix for easy level is 6x6 with 6 mines*/
                    selectedGameType = gameTypeEasy
                    numberOfRows = 6
                    numberOfColumn = 6
                    numberOfMines = 6
                }
                getString(R.string.medium) -> {
                    /*grid Matrix for Medium level is 10x10 with 10 mines*/
                    selectedGameType = gameTypeMedium
                    numberOfRows = 10
                    numberOfColumn = 10
                    numberOfMines = 10
                }
                getString(R.string.difficult) -> {
                    /*grid Matrix for Difficult level is 14x14 with 14 mines*/
                    selectedGameType = gameTypeHard
                    numberOfRows = 14
                    numberOfColumn = 14
                    numberOfMines = 14
                }
            }
            // If the Input is not as Per Required showing a Toast
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
                    "Please Enter Rows, Column and Mines greater or equal to 3 ",
                    Toast.LENGTH_SHORT
                ).show()
                return
            } else {
                numberOfRows = Integer.parseInt(rows)
                numberOfColumn = Integer.parseInt(column)
                numberOfMines = Integer.parseInt(mines)

                if (numberOfRows < 3 || numberOfColumn < 3 || numberOfMines < 3) {
                    Toast.makeText(
                        this@MainActivity,
                        "Please Enter Rows, Column and Mines greater or equal to 3 ",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
            }
        }

        /*Starting Game Activity Intent Region*/
        /*Creating an Intent by putting Extras like keyValue pair i.e putExtra(key, value)*/
        val intent: Intent = Intent(this@MainActivity, GameActivity::class.java).apply {
            /*sending data for no. of Rows column and mines for using on gameActivity*/
            putExtra(rows, numberOfRows)
            putExtra(column, numberOfColumn)
            putExtra(mines, numberOfMines)
        }
        startActivity(intent) // start the Activity by passing the intent in startActivity()
        /*Ending Game Activity IntentRegion*/
    }

    /*Update Time is called onResume() because when we came back
    * from GameActivity to MainAActivity onResume is called and Time will update.
    * */
    override fun onResume() {
        super.onResume()
        /*Update Time is called here*/
        updateTime()
    }

    private fun showInstructions() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("INSTRUCTIONS")
        builder.setMessage(
            "The purpose of the game is to open all the cells of the board which do not contain a bomb. You lose if you set off a bomb cell.\n" +
                    "\n" +
                    "Every non-bomb cell you open will tell you the total number of bombs in the eight neighboring cells. Once you are sure that a cell contains a bomb, you can right-click to put a flag it on it as a reminder. Once you have flagged all the bombs around an open cell, you can quickly open the remaining non-bomb cells by shift-clicking on the cell.\n" +
                    "\n" +
                    "To start a new game (abandoning the current one), just click on the yellow face button.\n" +
                    "\n" +
                    "Happy Gaming!"
        )

        builder.setCancelable(false)
        builder.setPositiveButton(
            "OK"
        ) { _, _ ->
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }
}