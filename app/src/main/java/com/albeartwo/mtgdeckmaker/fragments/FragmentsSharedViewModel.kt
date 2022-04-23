package com.albeartwo.mtgdeckmaker.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.albeartwo.mtgdeckmaker.adapters.CardListAdapter
import com.albeartwo.mtgdeckmaker.adapters.CardListener
import com.albeartwo.mtgdeckmaker.database.Repository
import com.albeartwo.mtgdeckmaker.databinding.FragmentDisplayCardBinding
import com.albeartwo.mtgdeckmaker.databinding.FragmentSearchResultsBinding
import com.albeartwo.mtgdeckmaker.generated.Data
import com.albeartwo.mtgdeckmaker.generated.GetCardList
import com.albeartwo.mtgdeckmaker.network.ScryfallApiService
import com.albeartwo.mtgdeckmaker.other.UtilityClass
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.fragment_search_results.*
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository : Repository,
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

@AndroidEntryPoint
class ResultListFragment : Fragment() {

    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? , savedInstanceState : Bundle?
    ) : View {

        val binding = FragmentSearchResultsBinding.inflate(inflater)

        sharedViewModel.currentDeckId = ResultListFragmentArgs.fromBundle(requireArguments()).currentDeckId

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
        binding.viewModel = sharedViewModel

        val args = DisplayCardFragmentArgs.fromBundle(requireArguments()).fromFragment

        //Checks what fragment was used to navigate here
        when (args) {

            "results" -> {}
            else      -> {

                sharedViewModel.getSingleCardData(args)
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

