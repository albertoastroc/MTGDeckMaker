package com.albeartwo.mtgdeckmaker.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CardDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCard(card : Card) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeck(deck : Deck) : Long

    @Query("UPDATE deck_table SET deck_name = :deckName WHERE deck_db_id = :deckId")
    suspend fun changeDeckName(deckName : String , deckId : Int)

    @Query("SELECT * from deck_table ORDER BY deck_db_id DESC")
    fun getDecksList() : LiveData<List<Deck>>

    @Query("SELECT EXISTS (SELECT 1 FROM deck_card_cross_ref WHERE oracle_id = :oracleId AND deck_db_id = :deckId )")
    suspend fun cardExists(oracleId : String , deckId : Int) : Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM deck_table WHERE UPPER (deck_name) LIKE (:deckName))")
    suspend fun deckExists(deckName : String) : Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeckCardCrossRef(crossRef : DeckCardCrossRef)

    //deletes one card from cross ref
    @Query("DELETE FROM deck_card_cross_ref WHERE oracle_id = :oracleId AND deck_db_id = :deckId")
    suspend fun deleteFromCrossRef(oracleId : String , deckId : Int?)

    //deletes one card from card table
    @Query("DELETE FROM card_table WHERE card_db_id = :cardDbId")
    suspend fun removeFromDatabase(cardDbId : Int?) : Int

    //gets ids for cards to be deleted
    @Query("SELECT card_db_id FROM deck_card_cross_ref WHERE deck_db_id = :deckId")
    suspend fun getCardDbIdsToDelete(deckId : Int) : Array<Int>

    //deletes multiple cards from card table
    @Query("DELETE FROM card_table WHERE card_db_id in (:idList)")
    suspend fun deleteDeckContentsFromCardTable(idList : Array<Int>)

    //deletes multiple cards from cross ref
    @Query("DELETE FROM deck_card_cross_ref WHERE card_db_id in (:idList)")
    suspend fun deleteDeckContentsFromCrossRef(idList : Array<Int>)

    //deletes deck from deck table
    @Query("DELETE FROM deck_table WHERE deck_db_id is (:deckId)")
    suspend fun deleteDeckFromDeckTable(deckId : Int)

    @Transaction
    @Query("SELECT * FROM deck_table WHERE deck_db_id = :deckId")
    fun getCardsOfDeck(deckId : Int) : LiveData<List<DeckWithCards>>

    @Query("SELECT * FROM card_table WHERE card_db_id = :cardDbId")
    fun getSingleCardCount(cardDbId : Long) : LiveData<Card>

    @Query("UPDATE card_table SET card_count = card_count + 1 WHERE card_db_id = :cardDbId")
    suspend fun plusOneCardQuantity(cardDbId : Int?) : Int

    @Query("UPDATE card_table SET card_count = CASE WHEN card_count >= 1 THEN card_count -1 ELSE card_count END WHERE card_db_id = :cardDbId")
    suspend fun minusOneCardQuantity(cardDbId : Int?) : Int


}