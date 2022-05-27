package com.albeartwo.mtgdeckmaker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albeartwo.mtgdeckmaker.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedDecksViewModel @Inject constructor(
    private val repository : Repository
) : ViewModel() {

    val decks = repository.dbGetDecksList()

    fun insertDeck(deckName : String) {

        viewModelScope.launch {

            repository.dbInsertDeck(deckName)
        }

    }
}
