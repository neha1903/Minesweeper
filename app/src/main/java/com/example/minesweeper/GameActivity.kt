package com.example.minesweeper


import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.minesweeper.MainActivity.Companion.column
import com.example.minesweeper.MainActivity.Companion.mines
import com.example.minesweeper.MainActivity.Companion.rows
import com.example.minesweeper.adapters.GridRecyclerViewAdapter
import com.example.minesweeper.databinding.ActivityGameBinding
import com.example.minesweeper.interfaces.OnBlockClickListener
import com.example.minesweeper.manager.MineSweeperManager
import com.example.minesweeper.model.Block


/*Game Board Activity implementing Interface OnBlockClickListener */
class GameActivity : AppCompatActivity(), OnBlockClickListener {

    companion object {
        const val BEST_GAME_TIME = "BEST_TIME"
        const val LAST_GAME_TIME = "LAST_GAME_TIME"
    }

    /* Variable Declaration and Initialization Region Start */
    private lateinit var binding: ActivityGameBinding
    private val timerLength = 999000L // 999 seconds in milliseconds
    private lateinit var countDownTimer: CountDownTimer
    private var secondsElapsed = 0
    private var timerStarted = false
    private lateinit var gridRecyclerViewAdapter: GridRecyclerViewAdapter

    /*This ia main Manger Class Responsible for creating Grid or Mine Board
    * This class Handle all the features such as inserting mines creating mines
    * checking the game Won Status
    * Checking the block is flagged, reveled i.e state of the Block
    * Resetting Grid, clearing Grid
    *  */
    private lateinit var mineSweeperManager: MineSweeperManager
    /* Variable Declaration and Initialization Region End */

    /*OnCreate Region Start*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initializing binding variable This creates an Instance of ActivityGameBinding Class
        binding = ActivityGameBinding.inflate(layoutInflater)
        // Get a reference to the root view by calling the GetRoot
        val view = binding.root
        // pass view to setContentView() for making the view active on Screen
        setContentView(view)

        /* Get the Intent that started this activity and extract the Ints i.e is Rows, Columns and Mines */
        val rows: Int = intent.getIntExtra(rows, 0)
        val column: Int = intent.getIntExtra(column, 0)
        val mines: Int = intent.getIntExtra(mines, 0)

        // using Instance of the ActivityMainBinding class for calling any of the views in XML
        binding.apply {
            /*Grid creating Region Start*/
            gameGrid.layoutManager = GridLayoutManager(this@GameActivity, rows)
            mineSweeperManager = MineSweeperManager(rows, column, mines)

            gridRecyclerViewAdapter =
                GridRecyclerViewAdapter(
                    mineSweeperManager.getGrid().getBlockList(),
                    this@GameActivity
                )
            gameGrid.adapter = gridRecyclerViewAdapter
            /*Grid creating Region End*/

            /*CountDown Timer Region Start*/
            timerStarted = false
            countDownTimer = object : CountDownTimer(timerLength, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    secondsElapsed += 1
                    activityGameTimer.text = String.format("%03d", secondsElapsed)
                }

                override fun onFinish() {
                    mineSweeperManager.outOfTime()
                    Toast.makeText(
                        applicationContext,
                        "Game Over: Timer Expired, Keep Trying!",
                        Toast.LENGTH_SHORT
                    ).show()
                    mineSweeperManager.getGrid().revealAllBombs()
                    gridRecyclerViewAdapter.setBlocks(mineSweeperManager.getGrid().getBlockList())
                }
            }
            /*CountDown Timer Region End*/
            // showing Flag Counts
            activityMainFlagsLeft.text = String.format(
                "%03d",
                mineSweeperManager.getNumberBombs() - mineSweeperManager.flagCount
            )

            /*On Smiley Click The Game Will Restart*/
            activityMainSmiley.setOnClickListener {
                mineSweeperManager = MineSweeperManager(rows, column, mines)
                gridRecyclerViewAdapter.setBlocks(mineSweeperManager.getGrid().getBlockList())
                timerStarted = false
                countDownTimer.cancel()
                updateTime()
                secondsElapsed = 0
                activityGameTimer.setText(R.string.default_count)
                activityMainFlagsLeft.text = String.format(
                    "%03d",
                    mineSweeperManager.getNumberBombs() - mineSweeperManager.flagCount
                )
            }


            /*On Click For Adding Flags on the Grid*/
            activityMainFlag.setOnClickListener {
                mineSweeperManager.toggleMode()
                if (mineSweeperManager.isFlagMode) {
                    val border = GradientDrawable()
                    border.setColor(-0x1)
                    border.setStroke(1, -0x1000000)
                    activityMainFlag.background = border
                } else {
                    val border = GradientDrawable()
                    border.setColor(-0x1)
                    activityMainFlag.background = border
                }
            }

        }
    }
    /*On Create Region End*/

    /*Definition of BlockClick Function Region Start
    * Member of OnBlockClick listener */
    override fun blockClick(block: Block) {
        binding.apply {
            mineSweeperManager.handleBlockClick(block)
            activityMainFlagsLeft.text = String.format(
                "%03d",
                mineSweeperManager.getNumberBombs() - mineSweeperManager.flagCount
            )
            if (!timerStarted) {
                countDownTimer.start()
                timerStarted = true
            }
            if (mineSweeperManager.isGameOver) {
                countDownTimer.cancel()
                updateTime()
                Toast.makeText(applicationContext, "Game Over", Toast.LENGTH_SHORT).show()
                mineSweeperManager.getGrid().revealAllBombs()
            }
            if (mineSweeperManager.isGameWon) {
                countDownTimer.cancel()
                updateTime()
                Toast.makeText(applicationContext, "Game Won", Toast.LENGTH_SHORT).show()
                mineSweeperManager.getGrid().revealAllBombs()
            }
            gridRecyclerViewAdapter.setBlocks(mineSweeperManager.getGrid().getBlockList())

        }
    }
    /*Definition of BlockClick Function Region Start*/


    private fun updateTime() {
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val bestTime = sharedPreferences.getInt(BEST_GAME_TIME, 0)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val currentTime = Integer.parseInt(binding.activityGameTimer.text.toString())
        if (mineSweeperManager.isGameWon && bestTime < currentTime) {
            editor.putInt(
                BEST_GAME_TIME,
                Integer.parseInt(binding.activityGameTimer.text.toString())
            )
        }
        editor.putInt(LAST_GAME_TIME, Integer.parseInt(binding.activityGameTimer.text.toString()))
        editor.apply()
    }

}