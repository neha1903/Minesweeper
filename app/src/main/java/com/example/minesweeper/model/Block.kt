package com.example.minesweeper.model

/*Simple class for Each Block in the Grid*/
class Block(private var value: Int) {
    var isRevealed = false
    var isFlagged = false

    companion object {
        const val BOMB = -1
        const val BLANK = 0
    }

    fun getValue(): Int {
        return value
    }
}


