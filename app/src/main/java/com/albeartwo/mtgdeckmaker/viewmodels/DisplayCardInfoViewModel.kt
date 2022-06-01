package com.albeartwo.mtgdeckmaker.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.albeartwo.mtgdeckmaker.database.Card
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

    val _isCreatureOrPlaneswalker = MutableLiveData<Boolean>()

    val isCreatureOrPlaneswalker : LiveData<Boolean>
        get() = _isCreatureOrPlaneswalker

    val _isPlaneswalker = MutableLiveData<Boolean>()

    val isPlaneswalker : LiveData<Boolean>
        get() = _isPlaneswalker

    val _card = MutableLiveData<Card>()

    val _manaSymbols = MutableLiveData<List<String>>()

    val manaSymbols : LiveData<List<String>>
        get() = _manaSymbols

    private fun isCreatureOrPlaneswalker() {

        singleCardData.value?.type_line.apply {  }

        if (singleCardData.value?.type_line?.contains("Creature") == true || (singleCardData.value?.type_line?.contains("Planeswalker") == true)
        )
            _isCreatureOrPlaneswalker.value = true
    }

    private fun isPlaneswalker() {

        if (singleCardData.value?.type_line?.contains("Planeswalker") == true)
        _isPlaneswalker.value = true
    }

    private fun getManaSymbols() {

        _manaSymbols.value = _singleCardData.value?.mana_cost?.let { UtilityClass.getCmcArray(it) }
    }

    private suspend fun checkCardInDeck() {

        _inDeck.value = singleCardData.value?.let { currentDeckId?.let { deckId -> repository.dbCardExists(it.oracle_id , deckId) } }
    }

    fun getSingleCardData(query : String , context : Context) {

        if (isNetworkAvailable(context)) {

            viewModelScope.launch {

                val result = repository.nwGetSingleCardImage(query)
                _singleCardData.value = result
                isCreatureOrPlaneswalker()
                isPlaneswalker()
                getManaSymbols()
                checkCardInDeck()
            }
        }
    }

    fun saveCard() {

        val card = singleCardData.value?.let { UtilityClass.convertDataToCard(it) }

        viewModelScope.launch {

            card?.let { currentDeckId?.let { deckId -> repository.insertCardIntoDb(card , deckId) } }
            checkCardInDeck()
        }
    }

    private fun isNetworkAvailable(context : Context) : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)      -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)  -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)  -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else                                                        -> false
        }
    }

}

