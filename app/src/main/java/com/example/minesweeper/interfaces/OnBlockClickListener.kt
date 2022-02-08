package com.example.minesweeper.interfaces

import com.example.minesweeper.model.Block

interface OnBlockClickListener {
    fun blockClick(block: Block)
}