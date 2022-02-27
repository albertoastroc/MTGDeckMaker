package com.albeartwo.mtgdeckmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.albeartwo.mtgdeckmaker.database.Deck
import com.albeartwo.mtgdeckmaker.databinding.SavedDecksListItemBinding

class DecksListAdapter(val clickListener : DecksListener) : ListAdapter<Deck , DecksListAdapter.DecksViewHolder>(DiffCallback) {

    class DecksViewHolder(private var binding : SavedDecksListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(deck : Deck , clickListener : DecksListener) {
            binding.deck = deck
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Deck>() {
        override fun areItemsTheSame(oldItem : Deck , newItem : Deck) : Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem : Deck , newItem : Deck) : Boolean {
            return oldItem.deckId == newItem.deckId
        }
    }


    override fun onCreateViewHolder(
        parent : ViewGroup ,
        viewType : Int
    ) : DecksViewHolder {
        return DecksViewHolder(SavedDecksListItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    //Replaces the contents of a view
    override fun onBindViewHolder(holder : DecksViewHolder , position : Int) {
        val deck = getItem(position)
        holder.bind(deck , clickListener)
    }
}


class DecksListener(val clickListener : (singleDeckData : Deck) -> Unit) {

    fun onClick(deck : Deck) {

        clickListener(deck)
    }
}