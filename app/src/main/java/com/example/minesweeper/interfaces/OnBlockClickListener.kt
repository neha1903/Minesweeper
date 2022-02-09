package com.example.minesweeper.interfaces

import com.example.minesweeper.model.Block

/*Created an interface for on Click on Each Block of the Grid*/
interface OnBlockClickListener {
    fun blockClick(block: Block) // function Declaration
}