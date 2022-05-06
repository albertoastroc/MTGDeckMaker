package com.albeartwo.mtgdeckmaker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albeartwo.mtgdeckmaker.database.Repository
import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import com.albeartwo.mtgdeckmaker.other.UtilityClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository : Repository ,
) : ViewModel() {

    val _cardList = MutableLiveData<GetCardList?>()

    val cardList : LiveData<GetCardList?>
        get() = _cardList

    val _singleCardData = MutableLiveData<Data?>()

    val singleCardData : LiveData<Data?>
        get() = _singleCardData

    var currentDeckId : Int = 0

    fun getSearchResults(query : String) {

        viewModelScope.launch {

            val result = repository.nwGetSearchResultsList(query)
            _cardList.value = result
        }
    }

    fun getSingleCardData(query : String) {

        viewModelScope.launch {

            val result = repository.nwGetSingleCardImage(query)
            _singleCardData.value = result
        }
    }

    fun saveCard() {

        val card = singleCardData.value?.let { UtilityClass.convertDataToCard(it) }

        viewModelScope.launch {

            card?.let { repository.insertCardIntoDb(it , currentDeckId) }
        }
    }
}