/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.albeartwo.mtgdeckmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.albeartwo.mtgdeckmaker.database.Card
import com.albeartwo.mtgdeckmaker.databinding.DeckCardListItemBinding


/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 */
class DeckCardListAdapter(val clickListener : (Card , String) -> Unit) : ListAdapter<Card , DeckCardListAdapter.DeckCardsViewHolder>(DiffCallback) {

    class DeckCardsViewHolder(private var binding : DeckCardListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(card : Card , clickListener : (Card , String) -> Unit) {

            binding.card = card

            binding.currentCardCountTv.text = card.cardCount.toString()


            binding.addOneTv.setOnClickListener {

                clickListener(card , "plus_one")

            }

            binding.minusOneTv.setOnClickListener {

                clickListener(card , "minus_one")

            }

            binding.root.setOnClickListener {

                clickListener(card , "root")

            }


            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Card>() {
        override fun areItemsTheSame(oldItem : Card , newItem : Card) : Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem : Card , newItem : Card) : Boolean {
            return oldItem.cardDbId == newItem.cardDbId
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent : ViewGroup ,
        viewType : Int
    ) : DeckCardsViewHolder {
        return DeckCardsViewHolder(DeckCardListItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder : DeckCardsViewHolder , position : Int) {
        val card = getItem(position)
        holder.bind(card , clickListener)
    }

    override fun getItemId(position : Int) : Long {
        return getItem(position).cardDbId?.toLong() ?: getItemId(position)

    }

    override fun setHasStableIds(hasStableIds : Boolean) {
        super.setHasStableIds(true)
    }

}
