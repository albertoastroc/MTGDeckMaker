package com.albeartwo.mtgdeckmaker.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albeartwo.mtgdeckmaker.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewDeckViewModel @Inject constructor(
    private val repository : Repository,
) : ViewModel() {

    fun insertDeck(deckName : String) {

        viewModelScope.launch {

            if (! repository.dbDeckExists(deckName)) {

                repository.dbInsertDeck(deckName)
            }
        }
    }
}