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
import com.albeartwo.mtgdeckmaker.databinding.FragmentDatabaseDecksBinding
import com.albeartwo.mtgdeckmaker.viewmodels.SavedDecksViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SavedDecksFragment : Fragment() {

    private val viewModel : SavedDecksViewModel by viewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {

        val binding = FragmentDatabaseDecksBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.decksListView.adapter = DecksListAdapter(DecksListener { singleDeckData ->

            if (singleDeckData.deckId != null) {
                findNavController().navigate(

                    SavedDecksFragmentDirections.actionSavedDecksFragmentToDeckCardListFragment(
                        singleDeckData.deckId!!
                    )
                )
            }
            viewModel._singleDeck.value = singleDeckData
        })

        binding.savedDecksFab.setOnClickListener {

            findNavController().navigate(SavedDecksFragmentDirections.actionSavedDecksFragmentToEditDeckFragment(0))
        }

        return binding.root
    }

}