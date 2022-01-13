package com.albeartwo.mtgdeckmaker.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CardWithDecks(

    @Embedded val card : Card ,
    @Relation(
        parentColumn = "cardId",
        entityColumn = "deckId",
        associateBy = Junction(DeckCardCrossRef::class)
    )
    val decks : List<Deck>

)