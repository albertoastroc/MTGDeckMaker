package com.albeartwo.mtgdeckmaker.database

import androidx.lifecycle.LiveData
import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import com.albeartwo.mtgdeckmaker.network.ScryfallApiService
import javax.inject.Inject

class Repository @Inject constructor(
    private val cardDatabaseDao : CardDatabaseDao ,
    private val scryfallAPI : ScryfallApiService
) {


    //this
    suspend fun dbInsertCardCardTable(card : Card) : Long = cardDatabaseDao.insertCard(card)

    //this
    suspend fun dbInsertDeckCardCrossRef(crossRef : DeckCardCrossRef) = cardDatabaseDao.insertDeckCardCrossRef(crossRef)

    suspend fun insertCardIntoDb(card : Card , currentDeckId : Int) {

        val oracleId = card.oracleId

        if (! dbCardExists(oracleId , currentDeckId)) {

            val dbId = card.let { dbInsertCardCardTable(it) }
            val deckCardRelation = DeckCardCrossRef(currentDeckId , dbId , oracleId)
            deckCardRelation.let { dbInsertDeckCardCrossRef(it) }

        }
    }


    suspend fun dbDeleteCardFromCardTable(cardDbId : Int?) : Int = cardDatabaseDao.removeFromDatabase(cardDbId)

    suspend fun dbCardExists(oracleId : String , deckId : Int) : Boolean = cardDatabaseDao.cardExists(oracleId , deckId)

    suspend fun dbDeckExists(deckName : String) : Boolean = cardDatabaseDao.deckExists(deckName)

    suspend fun dbInsertDeck(deck : Deck) = cardDatabaseDao.insertDeck(deck)

    fun dbGetDecksList() : LiveData<List<Deck>> = cardDatabaseDao.getDecksList()

    fun dbGetCardsOfDeck(deckId : Int) : LiveData<List<DeckWithCards>> = cardDatabaseDao.getCardsOfDeck(deckId)

    suspend fun dbDeleteCrossRef(oracleId : String , deckId : Int) = cardDatabaseDao.deleteFromCrossRef(oracleId , deckId)

    suspend fun dbAddOneCardQuantity(cardDbId : Int?) : Int = cardDatabaseDao.plusOneCardQuantity(cardDbId)

    suspend fun dbSubtractOneCardQuantity(cardDbId : Int?) : Int = cardDatabaseDao.minusOneCardQuantity(cardDbId)

    suspend fun updateDeckName(deckName : String , deckId : Int) = cardDatabaseDao.changeDeckName(deckName , deckId)

    suspend fun dbDeleteDeck(deckId : Int) {

        val ids = cardDatabaseDao.getCardDbIdsToDelete(deckId)
        cardDatabaseDao.deleteDeckContentsFromCardTable(ids)
        cardDatabaseDao.deleteDeckContentsFromCrossRef(ids)
        cardDatabaseDao.deleteDeckFromDeckTable(deckId)

    }

    suspend fun dbDeleteDeckContentsFromCardTable(idList : Array<Int>) = cardDatabaseDao.deleteDeckContentsFromCardTable(idList)

    suspend fun dbDeleteDeckContentsFromCrossRef(idList : Array<Int>) = cardDatabaseDao.deleteDeckContentsFromCrossRef(idList)

    suspend fun dbDeleteDeckFromDeckTable(deckId : Int) = cardDatabaseDao.deleteDeckFromDeckTable(deckId)

    suspend fun nwGetSearchResultsList(listQuery : String) : GetCardList? {

        val response = scryfallAPI.getCardListResults(listQuery)
        return response.body()
    }

    suspend fun nwGetSingleCardImage(imageQuery : String) : Data? {

        val response = scryfallAPI.getSingleCardData(imageQuery)
        return response.body()
    }
}