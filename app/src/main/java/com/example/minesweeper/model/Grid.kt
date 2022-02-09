package com.example.minesweeper.model

import java.util.*

class Grid(private val rows: Int, private val column: Int) {
    private val blocks: MutableList<Block>

    init {
        blocks = ArrayList<Block>()
        for (i in 0 until rows * column) {
            blocks.add(Block(Block.BLANK))
        }
    }

    fun insertBombInGrid(totalBombs: Int) {
        var bombsPlaced = 0
        while (bombsPlaced < totalBombs) {
            val x = Random().nextInt(rows)
            val y = Random().nextInt(column)
            if (blockAt(x, y)?.getValue() == Block.BLANK) {
                blocks[x + y * rows] = Block(Block.BOMB)
                bombsPlaced++
            }
        }
        for (x in 0 until rows) {
            for (y in 0 until column) {
                if (blockAt(x, y)?.getValue() != Block.BOMB) {
                    val adjacentBlocks: List<Block> = adjacentBlocks(x, y)
                    var countBombs = 0
                    for (block in adjacentBlocks) {
                        if (block.getValue() == Block.BOMB) {
                            countBombs++
                        }
                    }
                    if (countBombs > 0) {
                        blocks[x + y * rows] = Block(countBombs)
                    }
                }
            }
        }
    }

    fun adjacentBlocks(x: Int, y: Int): List<Block> {
        val adjacentBlocks: MutableList<Block> = ArrayList<Block>()
        val blockLists: MutableList<Block?> = ArrayList<Block?>()
        blockLists.add(blockAt(x - 1, y))
        blockLists.add(blockAt(x + 1, y))
        blockLists.add(blockAt(x - 1, y - 1))
        blockLists.add(blockAt(x, y - 1))
        blockLists.add(blockAt(x + 1, y - 1))
        blockLists.add(blockAt(x - 1, y + 1))
        blockLists.add(blockAt(x, y + 1))
        blockLists.add(blockAt(x + 1, y + 1))
        for (block in blockLists) {
            if (block != null) {
                adjacentBlocks.add(block)
            }
        }
        return adjacentBlocks
    }

    private fun blockAt(x: Int, y: Int): Block? {
        return if (x < 0 || x >= rows || y < 0 || y >= column) {
            null
        } else blocks[x + y * rows]
    }


    fun getBlockList(): List<Block> {
        return blocks
    }


    fun toXY(index: Int): IntArray {
        val y: Int = index / rows
        val x: Int = index - y * rows
        return intArrayOf(x, y)
    }

    fun revealAllBombs() {
        for (c in blocks) {
            if (c.getValue() == Block.BOMB) {
                c.isRevealed = true
            }
        }
    }

}


