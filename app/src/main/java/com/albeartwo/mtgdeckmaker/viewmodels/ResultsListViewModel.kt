package com.albeartwo.mtgdeckmaker.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import com.albeartwo.mtgdeckmaker.database.Repository
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ResultsListViewModel @Inject constructor(
    private val repository : Repository ,
    private val savedStateHandle : SavedStateHandle
) : ViewModel() {

    val deckId : Int? = savedStateHandle["current_deck_id"]

    private val _cardList = MutableLiveData<GetCardList?>()

    val cardList : LiveData<GetCardList?>
        get() = _cardList

    fun getSearchResults(query : String, context : Context) {

        if (isNetworkAvailable(context)) {

            viewModelScope.launch {

                val result = repository.nwGetSearchResultsList(query)
                _cardList.value = result
                Timber.d("$result")

            }
        } else {

            Toast.makeText(context, "Network not available", Toast.LENGTH_LONG).show()
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
