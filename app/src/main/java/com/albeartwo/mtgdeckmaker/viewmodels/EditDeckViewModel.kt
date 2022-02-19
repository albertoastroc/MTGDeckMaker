package com.albeartwo.mtgdeckmaker.viewmodels

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

@HiltViewModel
class EditDeckViewModel @Inject constructor(
    private val repository : Repository
) : ViewModel() {

    fun changeDeckName(deck : Deck){

        viewModelScope.launch {

            deck.deckId?.let { repository.updateDeckName(deck.deckName, it) }

        }

    }

    fun insertDeck(deck : Deck) {

        viewModelScope.launch {

            if(! repository.dbDeckExists(deck.deckName)){

                repository.dbInsertDeck(deck)
            }
        }

    }


}