package com.albeartwo.mtgdeckmaker.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albeartwo.mtgdeckmaker.database.Deck
import com.albeartwo.mtgdeckmaker.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditDeckViewModel @Inject constructor(
    private val repository : Repository,
    savedStateHandle : SavedStateHandle
) : ViewModel() {

    val deckId : Int? = savedStateHandle["currentDeckId"]

    fun changeDeckName(deckName : String , deckId : Int) {

        viewModelScope.launch {
            repository.updateDeckName(deckName , deckId)
        }
    }

    fun insertDeck(deck : Deck) {

        viewModelScope.launch {

            if (! repository.dbDeckExists(deck.deckName)) {

                repository.dbInsertDeck(deck)
            }
        }
    }

    fun deleteDeck(deckId : Int) {

        viewModelScope.launch {

            repository.dbDeleteDeck(deckId)

        }
    }
}