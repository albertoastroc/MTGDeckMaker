package com.albeartwo.mtgdeckmaker.database

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class DeckWithCards(

    @Embedded val deck : Deck ,
    @Relation(
         parentColumn = "deck_db_id",
         entityColumn = "card_db_id",
         associateBy = Junction(DeckCardCrossRef::class)
     )
    val cards : List<Card>

    )