package com.albeartwo.mtgdeckmaker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deck_table")
data class Deck(

    @ColumnInfo(name = "deck_name")
    var deckName : String = "",

)

{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "deck_db_id")
    var deckId : Int? = null

}


