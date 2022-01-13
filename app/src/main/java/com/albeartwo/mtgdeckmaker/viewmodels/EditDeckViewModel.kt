package com.albeartwo.mtgdeckmaker.viewmodels

import androidx.lifecycle.*
import com.albeartwo.mtgdeckmaker.database.Deck
import com.albeartwo.mtgdeckmaker.database.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditDeckViewModel @Inject constructor (
    private val repository : Repository
) : ViewModel()  {

   fun insertDeck(deck : Deck){

       viewModelScope.launch {

           repository.dbInsertDeck(deck)

       }

   }


    }