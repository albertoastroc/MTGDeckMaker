package com.albeartwo.mtgdeckmaker.viewmodels

import androidx.lifecycle.*
import com.albeartwo.mtgdeckmaker.database.Deck
import com.albeartwo.mtgdeckmaker.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedDecksViewModel @Inject constructor (
    private val repository : Repository
) : ViewModel()  {

    val decks = repository.dbGetDecksList()

    internal val _singleDeck = MutableLiveData<Deck>()

    val singleDeck : LiveData<Deck>
        get() = _singleDeck

}