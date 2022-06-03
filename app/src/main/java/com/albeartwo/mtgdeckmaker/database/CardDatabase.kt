package com.albeartwo.mtgdeckmaker.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Card::class , Deck::class , DeckCardCrossRef::class] , version = 2 , exportSchema = false)
abstract class CardDatabase : RoomDatabase() {

    abstract fun getCardDatabaseDao() : CardDao
}