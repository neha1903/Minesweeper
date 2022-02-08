package com.example.minesweeper.manager

import com.example.minesweeper.model.Block
import com.example.minesweeper.model.Grid

class MineSweeperManager(
    private val rows: Int,
    private val column: Int,
    private val numberBombs: Int
) {

    private var grid: Grid = Grid(rows, column)

    var isGameOver = false
    var isFlagMode = false
    var isClearMode = true
    var flagCount = 0

    private var timeExpired = false

    fun toggleMode() {
        isClearMode = !isClearMode
        isFlagMode = !isFlagMode
    }

    fun outOfTime() {
        timeExpired = true
    }

    fun getGrid(): Grid {
        return grid
    }

    fun getNumberBombs(): Int {
        return numberBombs
    }

    fun handleBlockClick(block: Block) {
        if (!isGameOver && !isGameWon && !timeExpired && !block.isRevealed) {
            if (isClearMode) {
                clear(block)
            } else if (isFlagMode) {
                flag(block)
            }
        }
    }

    private fun clear(block: Block) {
        val index: Int = getGrid().getBlockList().indexOf(block)
        getGrid().getBlockList()[index].isRevealed = true
        if (block.getValue() == Block.BOMB) {
            isGameOver = true
        } else if (block.getValue() == Block.BLANK) {
            val toClear: MutableList<Block> = ArrayList()
            val toCheckAdj: MutableList<Block> = ArrayList()
            toCheckAdj.add(block)
            while (toCheckAdj.size > 0) {
                val b: Block = toCheckAdj[0]
                val cellIndex: Int = getGrid().getBlockList().indexOf(b)
                val cellPos: IntArray = getGrid().toXY(cellIndex)
                for (adjacent in getGrid().adjacentBlocks(cellPos[0], cellPos[1])) {
                    if (adjacent.getValue() == Block.BLANK) {
                        if (!toClear.contains(adjacent)) {
                            if (!toCheckAdj.contains(adjacent)) {
                                toCheckAdj.add(adjacent)
                            }
                        }
                    } else {
                        if (!toClear.contains(adjacent)) {
                            toClear.add(adjacent)
                        }
                    }
                }
                toCheckAdj.remove(b)
                toClear.add(b)
            }
            for (c in toClear) {
                c.isRevealed = true
            }
        }
    }

    private fun flag(block: Block) {
        block.isFlagged = !block.isFlagged
        var count = 0
        for (b in getGrid().getBlockList()) {
            if (b.isFlagged) {
                count++
            }
        }
        flagCount = count
    }

    val isGameWon: Boolean
        get() {
            var numbersUnrevealed = 0
            for (b in getGrid().getBlockList()) {
                if (b.getValue() != Block.BOMB && b.getValue() != Block.BLANK && !b.isRevealed) {
                    numbersUnrevealed++
                }
            }
            return numbersUnrevealed == 0
        }

    init {
        grid = Grid(rows, column)
        grid.insertBombInGrid(numberBombs)
    }

}

