package com.albeartwo.mtgdeckmaker.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.albeartwo.mtgdeckmaker.adapters.CardListAdapter
import com.albeartwo.mtgdeckmaker.adapters.CardListener
import com.albeartwo.mtgdeckmaker.databinding.FragmentSearchResultsBinding
import com.albeartwo.mtgdeckmaker.viewmodels.ResultsListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultsFragment : Fragment() {

    private val resultsListViewModel : ResultsListViewModel by viewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? , savedInstanceState : Bundle?
    ) : View {

        val binding = FragmentSearchResultsBinding.inflate(inflater)

        resultsListViewModel.currentDeckId = ResultsFragmentArgs.fromBundle(requireArguments()).currentDeckId

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = resultsListViewModel

        binding.executeSearchIv.setOnClickListener {

            resultsListViewModel.getSearchResults(binding.searchInputEt.text.toString())
        }

        binding.cardList.adapter = CardListAdapter(CardListener { singleCardData ->
            view?.findNavController()?.navigate(
            ResultsFragmentDirections.actionResultListToDisplayCardInfoFragment(singleCardData.name.toString(), "results")
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

