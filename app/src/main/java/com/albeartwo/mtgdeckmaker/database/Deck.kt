package com.albeartwo.mtgdeckmaker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deck_table")
data class Deck(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "deck_id")
    var deckId : Int,

    @ColumnInfo(name = "deck_name")
    var deckName : String = "",

)


