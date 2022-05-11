package com.albeartwo.mtgdeckmaker.database

import androidx.room.Entity

@Entity(
    tableName = "deck_card_cross_ref" ,
    primaryKeys = ["deck_db_id" , "card_db_id"]
)

data class DeckCardCrossRef(

    val deck_db_id : Int ,
    val card_db_id : Long ,
    val oracle_id : String ,
)