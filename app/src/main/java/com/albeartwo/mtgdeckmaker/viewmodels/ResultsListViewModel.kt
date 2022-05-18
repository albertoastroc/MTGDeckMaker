package com.albeartwo.mtgdeckmaker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albeartwo.mtgdeckmaker.database.Repository
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsListViewModel @Inject constructor(
    private val repository : Repository ,
) : ViewModel() {

    val _cardList = MutableLiveData<GetCardList?>()

    val cardList : LiveData<GetCardList?>
        get() = _cardList

    var currentDeckId : Int = 0

    fun getSearchResults(query : String) {

        viewModelScope.launch {

            val result = repository.nwGetSearchResultsList(query)
            _cardList.value = result
        }
    }




}