

package com.albeartwo.mtgdeckmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.albeartwo.mtgdeckmaker.database.Card
import com.albeartwo.mtgdeckmaker.databinding.DeckCardListItemBinding


class DeckCardListAdapter(val clickListener : (Card , String) -> Unit) : ListAdapter<Card , DeckCardListAdapter.DeckCardsViewHolder>(DiffCallback) {

    class DeckCardsViewHolder(private var binding : DeckCardListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(card : Card , clickListener : (Card , String) -> Unit) {

            binding.card = card

            binding.currentCardCountTv.text = card.cardCount.toString()

            binding.addOneBt.setOnClickListener {

                clickListener(card , "plus_one")
            }

            binding.minusOneBt.setOnClickListener {

                clickListener(card , "minus_one")
            }

            binding.root.setOnClickListener {

                clickListener(card , "root")
            }

            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem : Card , newItem : Card) : Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem : Card , newItem : Card) : Boolean {
            return oldItem.cardDbId == newItem.cardDbId
        }
    }

    override fun onCreateViewHolder(
        parent : ViewGroup ,
        viewType : Int
    ) : DeckCardsViewHolder {
        return DeckCardsViewHolder(DeckCardListItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    //Replaces the contents of view
    override fun onBindViewHolder(holder : DeckCardsViewHolder , position : Int) {
        val card = getItem(position)
        holder.bind(card , clickListener)
    }

    override fun getItemId(position : Int) : Long {
        return getItem(position).cardDbId.toLong()
    }

    override fun setHasStableIds(hasStableIds : Boolean) {
        super.setHasStableIds(true)
    }
}
