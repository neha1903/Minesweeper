package com.example.minesweeper


import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.minesweeper.adapters.GridRecyclerViewAdapter
import com.example.minesweeper.databinding.ActivityGameBinding
import com.example.minesweeper.interfaces.OnBlockClickListener
import com.example.minesweeper.manager.MineSweeperManager
import com.example.minesweeper.model.Block


class GameActivity : AppCompatActivity(), OnBlockClickListener {

    private lateinit var binding: ActivityGameBinding

    private val TIMER_LENGTH = 999000L // 999 seconds in milliseconds
    private val BOMB_COUNT = 10
    private val GRID_ROW_SIZE = 10
    private val GRID_COLUMN_SIZE = 10

    private lateinit var countDownTimer: CountDownTimer
    private var secondsElapsed = 0
    private var timerStarted = false

    private lateinit var gridRecyclerViewAdapter: GridRecyclerViewAdapter
    private lateinit var mineSweeperManager: MineSweeperManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.apply {
            gameGrid.layoutManager = GridLayoutManager(this@GameActivity, 10)
            mineSweeperManager = MineSweeperManager(10, 10, 10)

            gridRecyclerViewAdapter =
                GridRecyclerViewAdapter(
                    mineSweeperManager.getGrid().getBlockList(),
                    this@GameActivity
                )
            gameGrid.adapter = gridRecyclerViewAdapter

            timerStarted = false
            countDownTimer = object : CountDownTimer(TIMER_LENGTH, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    secondsElapsed += 1
                    activityMainTimer.text = String.format("%03d", secondsElapsed)
                }

                override fun onFinish() {
                    mineSweeperManager.outOfTime()
                    Toast.makeText(
                        applicationContext,
                        "Game Over: Timer Expired",
                        Toast.LENGTH_SHORT
                    ).show()
                    mineSweeperManager.getGrid().revealAllBombs()
                    gridRecyclerViewAdapter.setBlocks(mineSweeperManager.getGrid().getBlockList())
                }
            }


            activityMainFlagsLeft.text = String.format(
                "%03d",
                mineSweeperManager.getNumberBombs() - mineSweeperManager.flagCount
            )



            activityMainSmiley.setOnClickListener {
                mineSweeperManager = MineSweeperManager(GRID_ROW_SIZE, GRID_COLUMN_SIZE, BOMB_COUNT)
                gridRecyclerViewAdapter.setBlocks(mineSweeperManager.getGrid().getBlockList())
                timerStarted = false
                countDownTimer.cancel()
                secondsElapsed = 0
                activityMainTimer.setText(R.string.default_count)
                activityMainFlagsLeft.text = String.format(
                    "%03d",
                    mineSweeperManager.getNumberBombs() - mineSweeperManager.flagCount
                )
            }


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
                Toast.makeText(applicationContext, "Game Over", Toast.LENGTH_SHORT).show()
                mineSweeperManager.getGrid().revealAllBombs()
            }
            if (mineSweeperManager.isGameWon) {
                countDownTimer.cancel()
                Toast.makeText(applicationContext, "Game Won", Toast.LENGTH_SHORT).show()
                mineSweeperManager.getGrid().revealAllBombs()
            }
            gridRecyclerViewAdapter.setBlocks(mineSweeperManager.getGrid().getBlockList())

        }
    }


}