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
import com.albeartwo.mtgdeckmaker.databinding.ListViewItemBinding
import com.albeartwo.mtgdeckmaker.generated.Data


/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 */
class CardListAdapter(val clickListener : CardListener) : ListAdapter<Data , CardListAdapter.CardViewHolder>(DiffCallback) {

    /**
     * The DecksViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [MarsProperty] information.
     */
    class CardViewHolder(private var binding : ListViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(card : Data , clickListener : CardListener) {
            binding.card = card
            binding.clickListener = clickListener
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MarsProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem : Data , newItem : Data) : Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem : Data , newItem : Data) : Boolean {
            return oldItem.id == newItem.id
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent : ViewGroup ,
        viewType : Int
    ) : CardViewHolder {
        return CardViewHolder(
            ListViewItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder : CardViewHolder , position : Int) {
        val card = getItem(position)
        holder.bind(card, clickListener)
    }
}


class CardListener(val clickListener : (singleCardData : Data) -> Unit) {

    fun onClick(data : Data){

        clickListener(data)


    }

}