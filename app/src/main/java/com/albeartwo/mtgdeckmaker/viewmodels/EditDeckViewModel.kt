package com.albeartwo.mtgdeckmaker.viewmodels

import android.util.Log.d
import android.widget.Toast
import androidx.lifecycle.*
import com.albeartwo.mtgdeckmaker.database.Deck
import com.albeartwo.mtgdeckmaker.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class EditDeckViewModel @Inject constructor(
    private val repository : Repository
) : ViewModel() {

    //TODO not working properly i think
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