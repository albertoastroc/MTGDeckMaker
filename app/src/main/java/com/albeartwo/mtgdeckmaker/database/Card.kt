package com.albeartwo.mtgdeckmaker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "card_table")
data class Card(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "card_db_id")
    var cardDbId : Long,

    @ColumnInfo(name = "oracle_id")
    var oracleId : String = "",

    @ColumnInfo(name = "card_count")
    val cardCount : Int,

    @ColumnInfo(name = "card_name")
    var cardName : String = "" ,

    @ColumnInfo(name = "oracle_text")
    var oracleText : String = "" ,

    @ColumnInfo(name = "type_line")
    var typeLine : String = "" ,

    // Can be null
    @ColumnInfo(name = "power")
    var power : String = "" ,

    // Can be null
    @ColumnInfo(name = "toughness")
    var toughness : String = "" ,

    @ColumnInfo(name = "thumbnail_url")
    var thumbnailUrl : String = ""

)

//Problem cards

//Cards that transform with day/night, 2 sided
//Overwhelmed Archivist // Archive Hunt - Does not show on preview, does not save text to db

