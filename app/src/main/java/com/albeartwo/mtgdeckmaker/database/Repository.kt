package com.albeartwo.mtgdeckmaker.database

import androidx.lifecycle.LiveData
import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import com.albeartwo.mtgdeckmaker.network.ScryfallApiService
import com.albeartwo.mtgdeckmaker.other.Resource
import java.lang.Exception
import javax.inject.Inject

class Repository @Inject constructor(
    private val cardDatabaseDao : CardDatabaseDao ,
    private val scryfallAPI : ScryfallApiService
) {


    //this
    suspend fun dbInsertCardCardTable(card : Card) : Long = cardDatabaseDao.insertCard(card)

    //this
    suspend fun dbInsertDeckCardCrossRef(crossRef : DeckCardCrossRef) = cardDatabaseDao.insertDeckCardCrossRef(crossRef)

    suspend fun insertCardIntoDb(card : Card, crossRef : DeckCardCrossRef) {



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

    suspend fun nwGetSearchResultsList(listQuery : String) : Resource<GetCardList> {

        return try {

            val response = scryfallAPI.getCardListResults(listQuery)

            if (response.isSuccessful) {

                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error has occured" , null)
            } else {

                Resource.error("An unknown error has occured" , null)
            }


        } catch (e : Exception) {
            Resource.error("Couldn't reach the server.  Check your internet connection" , null)
        }
    }

    fun nwGetSingleCardData(imageQuery : String) : Resource<Data> {

        return try {

            val response = scryfallAPI.getSingleCardData(imageQuery)

            if (response.isSuccessful) {

                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error has occured" , null)
            } else {

                Resource.error("An unknown error has occured" , null)
            }


        } catch (e : Exception) {
            Resource.error("Couldn't reach the server.  Check your internet connection" , null)
        }
    }
}