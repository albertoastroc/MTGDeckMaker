package com.albeartwo.mtgdeckmaker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albeartwo.mtgdeckmaker.database.Card
import com.albeartwo.mtgdeckmaker.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckCardListViewModel @Inject constructor(
    private var repository : Repository ,
    savedStateHandle : SavedStateHandle
) : ViewModel() {

    val deckId : Int? = savedStateHandle["current_deck_id"]

    val cardList = deckId?.let { repository.dbGetCardsOfDeck(it) }

    val card = MutableLiveData<Card>()

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

            deckId?.let {

                repository.dbDeleteCardFromCardTable(card.cardDbId)
                repository.dbDeleteCrossRef(card.oracleId , deckId)
            }



        }
    }
}