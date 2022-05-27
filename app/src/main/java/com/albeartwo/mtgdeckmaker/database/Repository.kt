package com.albeartwo.mtgdeckmaker.database

import androidx.lifecycle.LiveData
import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import com.albeartwo.mtgdeckmaker.network.ScryfallApiService
import retrofit2.Retrofit
import javax.inject.Inject

class Repository @Inject constructor(
    private val cardDatabaseDao : CardDatabaseDao ,
    private val retrofit : Retrofit
) {

    suspend fun dbInsertCardCardTable(card : Card) : Long = cardDatabaseDao.insertCard(card)

    suspend fun dbInsertDeckCardCrossRef(crossRef : DeckCardCrossRef) = cardDatabaseDao.insertDeckCardCrossRef(crossRef)

    suspend fun insertCardIntoDb(card : Card , currentDeckId : Int?) {

        val oracleId = card.oracleId

        currentDeckId?.let {

            if (! dbCardExists(oracleId , currentDeckId)) {

                val dbId = card.let { dbInsertCardCardTable(it) }
                val deckCardRelation = DeckCardCrossRef(currentDeckId , dbId , oracleId)
                deckCardRelation.let { dbInsertDeckCardCrossRef(it) }

            }
        }
    }

    suspend fun dbDeleteCardFromCardTable(cardDbId : Int?) : Int = cardDatabaseDao.removeFromDatabase(cardDbId)

    suspend fun dbCardExists(oracleId : String , deckId : Int) : Boolean = cardDatabaseDao.cardExists(oracleId , deckId)

    suspend fun dbDeckExists(deckName : String) : Boolean = cardDatabaseDao.deckExists(deckName)

    suspend fun dbInsertDeck(deckName : String) : Long {

        val newDeck = Deck(deckName)
        return cardDatabaseDao.insertDeck(newDeck)
    }

    fun dbGetDecksList() : LiveData<List<Deck>> = cardDatabaseDao.getDecksList()

    fun dbGetCardsOfDeck(deckId : Int) : LiveData<List<DeckWithCards>> = cardDatabaseDao.getCardsOfDeck(deckId)

    suspend fun dbDeleteCrossRef(oracleId : String , deckId : Int?) = cardDatabaseDao.deleteFromCrossRef(oracleId , deckId)

    suspend fun dbAddOneCardQuantity(cardDbId : Int?) : Int = cardDatabaseDao.plusOneCardQuantity(cardDbId)

    suspend fun dbSubtractOneCardQuantity(cardDbId : Int?) : Int = cardDatabaseDao.minusOneCardQuantity(cardDbId)

    suspend fun updateDeckName(deckName : String , deckId : Int) = cardDatabaseDao.changeDeckName(deckName , deckId)

    suspend fun dbDeleteDeck(deckId : Int) {

        val ids = cardDatabaseDao.getCardDbIdsToDelete(deckId)
        cardDatabaseDao.deleteDeckContentsFromCardTable(ids)
        cardDatabaseDao.deleteDeckContentsFromCrossRef(ids)
        cardDatabaseDao.deleteDeckFromDeckTable(deckId)

    }

    suspend fun nwGetSearchResultsList(query : String) : GetCardList? {

        return retrofit.create(ScryfallApiService::class.java).getCardListResults(query).body()
    }

    suspend fun nwGetSingleCardImage(query : String) : Data? {

        return retrofit.create(ScryfallApiService::class.java).getSingleCardData(query).body()
    }
}