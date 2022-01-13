package com.albeartwo.mtgdeckmaker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCard(card : Card) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeck(deck : Deck)

    @Query("SELECT * from deck_table ORDER BY deck_id DESC")
    fun getDecksList() : LiveData<List<Deck>>

    @Query("SELECT EXISTS (SELECT 1 FROM deck_card_cross_ref WHERE oracle_id = :oracleId AND deck_id = :deckId )")
    suspend fun exists(oracleId : String , deckId : Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeckCardCrossRef(crossRef : DeckCardCrossRef)

    @Query("DELETE FROM deck_card_cross_ref WHERE oracle_id = :oracleId AND deck_id = :deckId")
    suspend fun deleteFromCrossRef(oracleId : String, deckId : Int)

    @Transaction
    @Query("SELECT * FROM deck_table WHERE deck_id = :deckId")
    fun getCardsOfDeck(deckId : Int) : LiveData<List<DeckWithCards>>

    @Query("SELECT * FROM card_table WHERE card_db_id = :cardDbId")
    fun getSingleCardCount(cardDbId : Long) : LiveData<Card>

    @Query("UPDATE card_table SET card_count = card_count + 1 WHERE card_db_id = :cardDbId")
    suspend fun plusOneCardQuantity(cardDbId : Long) : Int

    @Query("UPDATE card_table SET card_count = CASE WHEN card_count >= 1 THEN card_count -1 ELSE card_count END WHERE card_db_id = :cardDbId")
    suspend fun minusOneCardQuantity(cardDbId : Long) : Int

    @Query("DELETE FROM card_table WHERE card_db_id = :cardDbId")
    suspend fun removeFromDatabase(cardDbId : Long) : Int


}