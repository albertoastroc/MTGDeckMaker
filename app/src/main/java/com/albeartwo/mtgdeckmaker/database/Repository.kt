package com.albeartwo.mtgdeckmaker.database

import androidx.lifecycle.LiveData
import com.albeartwo.mtgdeckmaker.network.CardApi
import javax.inject.Inject

class Repository @Inject constructor(
    val cardDatabaseDao : CardDatabaseDao
) {

    suspend fun dbInsertCardCardTable(card : Card) : Long = cardDatabaseDao.insertCard(card)

    suspend fun dbDeleteCardFromCardTable(cardDbId : Long) : Int = cardDatabaseDao.removeFromDatabase(cardDbId)

    suspend fun dbCardExists(oracleId : String , deckId : Int): Boolean = cardDatabaseDao.exists(oracleId, deckId)

    suspend fun dbInsertDeck(deck : Deck) = cardDatabaseDao.insertDeck(deck)

    fun dbGetDecksList() : LiveData<List<Deck>> = cardDatabaseDao.getDecksList()

    fun dbGetCardsOfDeck(deckId : Int) : LiveData<List<DeckWithCards>> = cardDatabaseDao.getCardsOfDeck(deckId)

    suspend fun dbInsertDeckCardCrossRef(crossRef : DeckCardCrossRef) = cardDatabaseDao.insertDeckCardCrossRef(crossRef)

    suspend fun dbDeleteCrossRef(oracleId : String, deckId : Int) = cardDatabaseDao.deleteFromCrossRef(oracleId, deckId)

    suspend fun dbAddOneCardQuantity(cardDbId : Long) : Int = cardDatabaseDao.plusOneCardQuantity(cardDbId)

    suspend fun dbSubtractOneCardQuantity(cardDbId : Long) : Int = cardDatabaseDao.minusOneCardQuantity(cardDbId)

    fun getSingleCardCount(cardDbId : Long) : LiveData<Card> = cardDatabaseDao.getSingleCardCount(cardDbId)

    fun nwGetSearchResultsList(inputText : String) = CardApi.retrofitService.getCardListResults(inputText)

    fun nwGetArtCropImage(cardName : String) = CardApi.retrofitService.getCardImage(cardName)

}