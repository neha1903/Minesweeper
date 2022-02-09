package com.example.minesweeper.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.minesweeper.R
import com.example.minesweeper.databinding.BlockItemBinding
import com.example.minesweeper.interfaces.OnBlockClickListener
import com.example.minesweeper.model.Block

/*Grid Adapter for binding View of Item_block to the Grid recycler View */
class GridRecyclerViewAdapter(
    private var blocks: List<Block>,
    private val onBlockClickListener: OnBlockClickListener
) : RecyclerView.Adapter<GridRecyclerViewAdapter.TileViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        val binding = BlockItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TileViewHolder(binding)
    }

    /*On Bind view Holder*/
    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        holder.bind(blocks[position])
        holder.setIsRecyclable(false)
    }

    /*Returns the total Number of blocks*/
    override fun getItemCount(): Int {
        return blocks.size
    }

    /*Set the List*/
    fun setBlocks(blocks: List<Block>) {
        this.blocks = blocks
        notifyDataSetChanged()
    }

    /*Internal ViewHolder Class*/
    inner class TileViewHolder(private val blockItemBinding: BlockItemBinding) :
        RecyclerView.ViewHolder(blockItemBinding.root) {
        fun bind(block: Block) {
            blockItemBinding.apply {
                mainLayout.setBackgroundColor(Color.GRAY)
                mainLayout.setOnClickListener {
                    onBlockClickListener.blockClick(block)
                }
                if (block.isRevealed) {
                    when {
                        block.getValue() == Block.BOMB -> {
                            itemBlockValue.setText(R.string.bomb)
                        }
                        block.getValue() == Block.BLANK -> {
                            itemBlockValue.text = ""
                            mainLayout.setBackgroundColor(Color.WHITE)
                        }
                        else -> {
                            itemBlockValue.text = block.getValue().toString()
                            when {
                                block.getValue() == 1 -> {
                                    itemBlockValue.setTextColor(Color.BLUE)
                                }
                                block.getValue() == 2 -> {
                                    itemBlockValue.setTextColor(Color.GREEN)
                                }
                                block.getValue() == 3 -> {
                                    itemBlockValue.setTextColor(Color.RED)
                                }
                            }
                        }
                    }
                } else if (block.isFlagged) {
                    itemBlockValue.setText(R.string.flag)
                }
            }
        }
    }

}


