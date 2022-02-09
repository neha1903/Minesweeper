package com.example.minesweeper


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


class GameActivity : AppCompatActivity(), OnBlockClickListener {

    private lateinit var binding: ActivityGameBinding

    private val timerLength = 999000L // 999 seconds in milliseconds


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

        val rows: Int = intent.getIntExtra(rows, 0)
        val column: Int = intent.getIntExtra(column, 0)
        val mines: Int = intent.getIntExtra(mines, 0)


        binding.apply {
            gameGrid.layoutManager = GridLayoutManager(this@GameActivity, rows)
            mineSweeperManager = MineSweeperManager(rows, column, mines)

            gridRecyclerViewAdapter =
                GridRecyclerViewAdapter(
                    mineSweeperManager.getGrid().getBlockList(),
                    this@GameActivity
                )
            gameGrid.adapter = gridRecyclerViewAdapter

            timerStarted = false
            countDownTimer = object : CountDownTimer(timerLength, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    secondsElapsed += 1
                    activityMainTimer.text = String.format("%03d", secondsElapsed)
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


            activityMainFlagsLeft.text = String.format(
                "%03d",
                mineSweeperManager.getNumberBombs() - mineSweeperManager.flagCount
            )



            activityMainSmiley.setOnClickListener {
                mineSweeperManager = MineSweeperManager(rows, column, mines)
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