package com.albeartwo.mtgdeckmaker.adapters

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.albeartwo.mtgdeckmaker.R
import com.albeartwo.mtgdeckmaker.database.Deck
import com.albeartwo.mtgdeckmaker.database.DeckWithCards
import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.other.UtilityClass
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import timber.log.Timber


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
            .diskCacheStrategy(DiskCacheStrategy.DATA)
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

@BindingAdapter("manaSymbols")
fun bindManaSymbols(linearLayout : LinearLayout, manaSymbols : String?){

    val resourceList = mutableListOf<Int>()

    val manaArray = manaSymbols?.let { UtilityClass.getCmcArray(it) }

    if (manaArray != null) {
        for (i in manaArray) {

            Timber.d(("first loop $i"))

            when (i) {

                "1" -> resourceList.add(R.drawable.ic_1_mana)
                "W" -> resourceList.add(R.drawable.ic_white)
                "B" -> resourceList.add(R.drawable.ic_black)

            }

        }
    }

    Timber.d("resource list $resourceList")
    Timber.d("mana array $manaArray")

    for (i in resourceList.indices) {
        val imageView = ImageView(linearLayout.context)
        linearLayout.addView(imageView)

        Timber.d("inside last loop $i")

        Glide.with(linearLayout.context)
            .load(resourceList[i])
            .override(200 , 200)
            .into(imageView)


    }



}