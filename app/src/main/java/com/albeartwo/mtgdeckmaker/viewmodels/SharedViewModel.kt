package com.albeartwo.mtgdeckmaker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albeartwo.mtgdeckmaker.database.Card
import com.albeartwo.mtgdeckmaker.database.Repository
import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import com.albeartwo.mtgdeckmaker.other.UtilityClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private var repository : Repository ,
) : ViewModel() {

    //Used by all

    var deckId = 0

    //Used by savedDecks

    val decks = repository.dbGetDecksList()

    fun insertDeck(deckName : String) {

        viewModelScope.launch {

            repository.dbInsertDeck(deckName)
        }

    }

    //Used by displayCardInfo

    val _singleCardData = MutableLiveData<Data?>()

    val singleCardData : LiveData<Data?>
        get() = _singleCardData

    val _manaSymbols = MutableLiveData<List<String>>()

    val manaSymbols : LiveData<List<String>>
        get() = _manaSymbols

    private fun getManaSymbols() {

        _manaSymbols.value = _singleCardData.value?.mana_cost?.let { UtilityClass.getCmcArray(it) }

    }

    fun getSingleCardData(query : String) {

        viewModelScope.launch {

            val result = repository.nwGetSingleCardImage(query)
            _singleCardData.value = result
            getManaSymbols()
        }
    }

    fun saveCard() {

        val card = singleCardData.value?.let { UtilityClass.convertDataToCard(it) }

        viewModelScope.launch {

            card?.let { repository.insertCardIntoDb(it , deckId) }
        }
    }

    //Used by DeckCardList

    val deckWithCards = repository.dbGetCardsOfDeck(deckId)

    internal val _singleCard = MutableLiveData<Card>()

    fun cardQuantAddOne(card : Card) {

        viewModelScope.launch {
            repository.dbAddOneCardQuantity(card.cardDbId)
        }
    }

    fun cardQuantMinusOne(card : Card) {

        viewModelScope.launch {
            repository.dbSubtractOneCardQuantity(card.cardDbId)
        }
    }

    fun removeFromDatabase(card : Card) {

        viewModelScope.launch {

            deckId.let {

                repository.dbDeleteCardFromCardTable(card.cardDbId)
                repository.dbDeleteCrossRef(card.oracleId , deckId)
            }
        }
    }

    //Used by results list

    val _resultsList = MutableLiveData<GetCardList?>()

    val resultsList : LiveData<GetCardList?>
        get() = _resultsList

    fun getSearchResults(query : String) {

        viewModelScope.launch {

            val result = repository.nwGetSearchResultsList(query)
            _resultsList.value = result
        }
    }

    //Used by edit deck

    fun changeDeckName(deckName : String , deckId : Int) {

        viewModelScope.launch {
            repository.updateDeckName(deckName , deckId)
        }
    }

    fun deleteDeck(deckId : Int) {

        viewModelScope.launch {

            repository.dbDeleteDeck(deckId)

        }
    }
}