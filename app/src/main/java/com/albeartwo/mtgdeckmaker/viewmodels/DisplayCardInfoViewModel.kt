package com.albeartwo.mtgdeckmaker.viewmodels

import androidx.lifecycle.*
import com.albeartwo.mtgdeckmaker.database.Repository
import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.other.UtilityClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayCardInfoViewModel @Inject constructor(
    private val repository : Repository ,
    private val savedStateHandle : SavedStateHandle
) : ViewModel() {

    var currentDeckId : Int? = savedStateHandle["current_deck_id"]

    val _singleCardData = MutableLiveData<Data?>()

    val singleCardData : LiveData<Data?>
        get() = _singleCardData

    val _inDeck = MutableLiveData<Boolean>()

    val inDeck : LiveData<Boolean>
        get() = _inDeck

    val _manaSymbols = MutableLiveData<List<String>>()

    val manaSymbols : LiveData<List<String>>
        get() = _manaSymbols

    private fun getManaSymbols() {

        _manaSymbols.value = _singleCardData.value?.mana_cost?.let { UtilityClass.getCmcArray(it) }

    }

    private suspend fun checkCardInDeck() {

        _inDeck.value = singleCardData.value?.let { repository.dbCardExists(it.oracle_id, currentDeckId!!) }
    }

    fun getSingleCardData(query : String) {

        viewModelScope.launch {

            val result = repository.nwGetSingleCardImage(query)
            _singleCardData.value = result
            getManaSymbols()
            checkCardInDeck()

        }
    }

    fun saveCard() {

        val card = singleCardData.value?.let { UtilityClass.convertDataToCard(it) }

        viewModelScope.launch {

            card?.let { repository.insertCardIntoDb(it , currentDeckId !!) }
        }
    }

}