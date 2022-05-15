package com.albeartwo.mtgdeckmaker.adapters

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.albeartwo.mtgdeckmaker.database.Deck
import com.albeartwo.mtgdeckmaker.database.DeckWithCards
import com.albeartwo.mtgdeckmaker.generated.Data
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop


@BindingAdapter("cardListData")
fun bindResultsRecyclerView(recyclerView : RecyclerView , data : List<Data>?) {

    val adapter = recyclerView.adapter as CardListAdapter
    adapter.submitList(data)
}

@BindingAdapter("deckListData")
fun bindDecksRecyclerView(recyclerView : RecyclerView , data : List<Deck>?) {

    val adapter = recyclerView.adapter as DecksListAdapter
    adapter.submitList(data?.reversed())
}

@BindingAdapter("deckCardsListData")
fun bindDeckCardsRecyclerView(recyclerView : RecyclerView , data : List<DeckWithCards>?) {

    val adapter = recyclerView.adapter as DeckCardListAdapter
    val cardList = data?.first()?.cards?.sortedWith(
        compareBy({ it.producedMana } , { it.power } , { it.typeLine })
    )
    adapter.submitList(cardList)
}

//Displays how many cards are in the deck if any
@SuppressLint("SetTextI18n")
@BindingAdapter("countAdapter")
fun countAdapter(textView : TextView , data : List<DeckWithCards>?) {

    var total = 0

    val cardListForCardCounter = data?.first()?.cards
    val deckName = data?.first()?.deck?.deckName

    if (cardListForCardCounter != null) {

        for ((index , cardCount) in cardListForCardCounter.withIndex()) {
            val currentCardCount = cardListForCardCounter[index].cardCount
            total += currentCardCount
        }
    }

    when (total) {

        0    -> textView.text = "$deckName is empty"
        1    -> textView.text = "1 card in $deckName"
        else -> textView.text = "$total cards in $deckName"
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView : ImageView , imgUrl : String?) {

    imgUrl?.let {

        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()

        Glide.with(imgView.context)
            .load(imgUri)
            .fitCenter()
            .into(imgView)
    }
}

@BindingAdapter("thumbnail")
fun bindThumbnail(imgView : ImageView , imgUrl : String?) {

    imgUrl?.let {

        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()

        Glide.with(imgView.context)
            .load(imgUri)
            .transform(CircleCrop())
            .into(imgView)
    }
}