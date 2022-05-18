package com.albeartwo.mtgdeckmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.albeartwo.mtgdeckmaker.adapters.DecksListAdapter
import com.albeartwo.mtgdeckmaker.adapters.DecksListener
import com.albeartwo.mtgdeckmaker.databinding.FragmentSavedDecksBinding
import com.albeartwo.mtgdeckmaker.viewmodels.SavedDecksViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SavedDecksFragment : Fragment() {

    private val viewModel : SavedDecksViewModel by viewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {

        val binding = FragmentSavedDecksBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.decksListView.adapter = DecksListAdapter(DecksListener { singleDeckData ->

            singleDeckData.deckId.let {

                findNavController().navigate(SavedDecksFragmentDirections.actionSavedDecksFragmentToDeckCardListFragment(it))

            }

        })

        binding.savedDecksFab.setOnClickListener {

            //0 tells EditDeckFragment that it was started to add a new deck
            findNavController().navigate(SavedDecksFragmentDirections.actionSavedDecksFragmentToAddNewDeckFragment(0))
        }

        return binding.root
    }

}