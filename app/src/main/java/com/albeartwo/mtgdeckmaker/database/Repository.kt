package com.albeartwo.mtgdeckmaker.database

import androidx.lifecycle.LiveData
import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import com.albeartwo.mtgdeckmaker.network.ScryfallApiService
import retrofit2.Retrofit
import javax.inject.Inject

class Repository @Inject constructor(
    private val cardDao : CardDao ,
    private val retrofit : Retrofit
) {

    suspend fun dbInsertCardCardTable(card : Card) : Long = cardDao.insertCard(card)

    suspend fun dbInsertDeckCardCrossRef(crossRef : DeckCardCrossRef) = cardDao.insertDeckCardCrossRef(crossRef)

    suspend fun insertCardIntoDb(card : Card , currentDeckId : Int) {

        val oracleId = card.oracleId

        if (! dbCardExists(oracleId , currentDeckId)) {

            val dbId = card.let { dbInsertCardCardTable(it) }
            val deckCardRelation = DeckCardCrossRef(currentDeckId , dbId , oracleId)
            deckCardRelation.let { dbInsertDeckCardCrossRef(it) }

        }
    }

    suspend fun dbDeleteCardFromCardTable(cardDbId : Int?) : Int = cardDao.removeFromDatabase(cardDbId)

    suspend fun dbCardExists(oracleId : String , deckId : Int) : Boolean = cardDao.cardExists(oracleId , deckId)

    suspend fun dbInsertDeck(deckName : String) : Long {

        val newDeck = Deck(deckName)
        return cardDao.insertDeck(newDeck)
    }

    fun dbGetDecksList() : LiveData<List<Deck>> = cardDao.getDecksList()

    fun dbGetCardsOfDeck(deckId : Int) : LiveData<List<DeckWithCards>> = cardDao.getCardsOfDeck(deckId)

    suspend fun dbDeleteCrossRef(oracleId : String , deckId : Int) = cardDao.deleteFromCrossRef(oracleId , deckId)

    suspend fun dbAddOneCardQuantity(cardDbId : Int?) : Int = cardDao.plusOneCardQuantity(cardDbId)

    suspend fun dbSubtractOneCardQuantity(cardDbId : Int?) : Int = cardDao.minusOneCardQuantity(cardDbId)

    suspend fun updateDeckName(deckName : String , deckId : Int) = cardDao.changeDeckName(deckName , deckId)

    suspend fun dbDeleteDeck(deckId : Int) {

        val ids = cardDao.getCardDbIdsToDelete(deckId)
        cardDao.deleteDeckContentsFromCardTable(ids)
        cardDao.deleteDeckContentsFromCrossRef(ids)
        cardDao.deleteDeckFromDeckTable(deckId)

    }

    suspend fun nwGetSearchResultsList(query : String) : GetCardList? {

        return retrofit.create(ScryfallApiService::class.java).getCardListResults(query).body()
    }

    suspend fun nwGetSingleCardImage(query : String) : Data? {

        return retrofit.create(ScryfallApiService::class.java).getSingleCardData(query).body()
    }
}