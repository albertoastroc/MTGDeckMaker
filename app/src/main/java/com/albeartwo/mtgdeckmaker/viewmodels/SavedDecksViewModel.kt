package com.albeartwo.mtgdeckmaker.viewmodels

import androidx.lifecycle.*
import com.albeartwo.mtgdeckmaker.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedDecksViewModel @Inject constructor(
    repository : Repository
) : ViewModel() {

    val decks = repository.dbGetDecksList()
}