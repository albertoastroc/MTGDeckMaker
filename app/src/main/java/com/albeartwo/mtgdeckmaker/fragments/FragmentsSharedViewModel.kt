package com.albeartwo.mtgdeckmaker.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.albeartwo.mtgdeckmaker.adapters.CardListAdapter
import com.albeartwo.mtgdeckmaker.adapters.CardListener
import com.albeartwo.mtgdeckmaker.database.DeckCardCrossRef
import com.albeartwo.mtgdeckmaker.database.Repository
import com.albeartwo.mtgdeckmaker.databinding.FragmentDisplayCardBinding
import com.albeartwo.mtgdeckmaker.databinding.FragmentSearchResultsBinding
import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import com.albeartwo.mtgdeckmaker.other.UtilityClass
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Callback


@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository : Repository ,
) : ViewModel() {

    private val _properties = MutableLiveData<GetCardList>()

    val properties : LiveData<GetCardList>
        get() = _properties

    internal val _cardList = MutableLiveData<List<Data>>()

    val cardList : LiveData<List<Data>>
        get() = _cardList

    internal val _singleCardData = MutableLiveData<Data>()

    val singleCardData : LiveData<Data>
        get() = _singleCardData

    internal var currentDeckId : Int = 0

    fun saveCard() {

        val card = singleCardData.value?.let { UtilityClass.convertDataToCard(it) }

        viewModelScope.launch {

            val oracleId = card?.oracleId

            if (oracleId != null) {

                if (! oracleId.let { repository.dbCardExists(it , currentDeckId) }) {

                    val dbId = card.let { repository.dbInsertCardCardTable(it) }
                    val deckCardRelation = DeckCardCrossRef(currentDeckId , dbId , oracleId)
                    deckCardRelation.let { repository.dbInsertDeckCardCrossRef(it) }

                }
            }
        }
    }

    fun getSearchResults(searchInput : String) {


        repository.nwGetSearchResultsList(searchInput).enqueue(
            object : Callback<GetCardList> {
                override fun onResponse(
                    call : Call<GetCardList> ,
                    response : retrofit2.Response<GetCardList>
                ) {

                    _properties.value = response.body()
                    _cardList.value = properties.value?.data

                }

                override fun onFailure(call : Call<GetCardList> , t : Throwable) {

                }
            })
    }

    fun getSingleCard(cardName : String) {


        repository.nwGetArtCropImage(cardName).enqueue(
            object : Callback<Data> {
                override fun onResponse(
                    call : Call<Data> ,
                    response : retrofit2.Response<Data>
                ) {

                    _singleCardData.value = response.body()


                }

                override fun onFailure(call : Call<Data> , t : Throwable) {

                }
            })
    }
}

@AndroidEntryPoint
class ResultListFragment : Fragment() {

    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? , savedInstanceState : Bundle?
    ) : View {

        sharedViewModel._cardList.value = null

        sharedViewModel.currentDeckId = ResultListFragmentArgs.fromBundle(requireArguments()).currentDeckId

        val binding = FragmentSearchResultsBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = sharedViewModel

        binding.executeSearchIv.setOnClickListener {

            sharedViewModel.getSearchResults(binding.searchInputEt.text.toString())

        }

        binding.cardList.adapter = CardListAdapter(CardListener { singleCardData ->
            sharedViewModel._singleCardData.value = singleCardData
            view?.findNavController()?.navigate(
                ResultListFragmentDirections.actionResultListToDisplayCardFragment("results")
            )

        })

        return binding.root

    }

    override fun onPause() {
        super.onPause()

        //Hide the soft keyboard when this fragment disappears
        val imm : InputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken , 0)
    }
}

@AndroidEntryPoint
class DisplayCardFragment : Fragment() {

    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {



        val binding = FragmentDisplayCardBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = sharedViewModel as SharedViewModel



        val args = DisplayCardFragmentArgs.fromBundle(requireArguments()).fromFragment

        //Checks what fragment was used to navigate here
        when (args) {

            "results" -> {}
            else      -> {

                sharedViewModel.getSingleCard(args)
                binding.saveCardButton.visibility = View.INVISIBLE
                
            }
        }

        binding.saveCardButton.setOnClickListener {

            sharedViewModel.saveCard()

            it.findNavController().navigate(

                DisplayCardFragmentDirections.actionDisplayCardFragmentToDeckCardListFragment(
                    sharedViewModel.currentDeckId
                )
            )
        }

        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel._singleCardData.value = null

    }
}

