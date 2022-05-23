package com.albeartwo.mtgdeckmaker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albeartwo.mtgdeckmaker.database.Deck
import com.albeartwo.mtgdeckmaker.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewDeckViewModel @Inject constructor(
    private val repository : Repository,
) : ViewModel() {

    fun insertDeck(deck : Deck) {

        viewModelScope.launch {

            if (! repository.dbDeckExists(deck.deckName)) {

                repository.dbInsertDeck(deck)
            }
        }
    }
}