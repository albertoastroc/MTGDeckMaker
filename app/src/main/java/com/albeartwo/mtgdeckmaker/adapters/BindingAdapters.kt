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
    Timber.d("$data")
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

    linearLayout.removeAllViews()

    val resourceList = mutableListOf<Int>()

    val manaArray = manaSymbols?.let { UtilityClass.getCmcArray(it) }

    manaArray?.forEach { i ->

        when (i) {

            "1" -> resourceList.add(R.drawable.ic_1_mana)
            "2" -> resourceList.add(R.drawable.ic_2_mana)
            "3" -> resourceList.add(R.drawable.ic_3_mana)
            "4" -> resourceList.add(R.drawable.ic_4_mana)
            "5" -> resourceList.add(R.drawable.ic_5_mana)
            "6" -> resourceList.add(R.drawable.ic_6_mana)
            "7" -> resourceList.add(R.drawable.ic_7_mana)
            "8" -> resourceList.add(R.drawable.ic_8_mana)
            "9" -> resourceList.add(R.drawable.ic_9_mana)
            "10" -> resourceList.add(R.drawable.ic_10_mana)
            "11" -> resourceList.add(R.drawable.ic_11_mana)
            "12" -> resourceList.add(R.drawable.ic_12_mana)
            "13" -> resourceList.add(R.drawable.ic_13_mana)
            "14" -> resourceList.add(R.drawable.ic_14_mana)
            "15" -> resourceList.add(R.drawable.ic_15_mana)
            "16" -> resourceList.add(R.drawable.ic_16_mana)
            "17" -> resourceList.add(R.drawable.ic_17_mana)
            "18" -> resourceList.add(R.drawable.ic_18_mana)
            "19" -> resourceList.add(R.drawable.ic_19_mana)
            "20" -> resourceList.add(R.drawable.ic_20_mana)
            "R" -> resourceList.add(R.drawable.ic_red_mana)
            "U" -> resourceList.add(R.drawable.ic_blue_mana)
            "G" -> resourceList.add(R.drawable.ic_green_mana)
            "W" -> resourceList.add(R.drawable.ic_white_mana)
            "B" -> resourceList.add(R.drawable.ic_black_mana)

        }
    }

    for (i in resourceList.indices) {

        val imageView = ImageView(linearLayout.context)
        linearLayout.addView(imageView)

        Glide.with(linearLayout.context)
            .load(resourceList[i])
            .override(50 , 50)
            .into(imageView)
    }
}