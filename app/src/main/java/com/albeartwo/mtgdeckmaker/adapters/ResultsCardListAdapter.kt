package com.albeartwo.mtgdeckmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.albeartwo.mtgdeckmaker.databinding.ListViewItemBinding
import com.albeartwo.mtgdeckmaker.generated.Data

class CardListAdapter(val clickListener : CardListener) : ListAdapter<Data , CardListAdapter.CardViewHolder>(DiffCallback) {

    class CardViewHolder(private var binding : ListViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(card : Data , clickListener : CardListener) {
            binding.card = card
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem : Data , newItem : Data) : Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem : Data , newItem : Data) : Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent : ViewGroup ,
        viewType : Int
    ) : CardViewHolder {
        return CardViewHolder(
            ListViewItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ) , parent , false
            )
        )
    }

    //Replaces contents of a view
    override fun onBindViewHolder(holder : CardViewHolder , position : Int) {
        val card = getItem(position)
        holder.bind(card , clickListener)
    }
}


class CardListener(val clickListener : (singleCardData : Data) -> Unit) {

    fun onClick(data : Data) {

        clickListener(data)
    }
}