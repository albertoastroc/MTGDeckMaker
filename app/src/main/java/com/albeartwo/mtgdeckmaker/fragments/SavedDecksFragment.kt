package com.albeartwo.mtgdeckmaker.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.albeartwo.mtgdeckmaker.adapters.DecksListAdapter
import com.albeartwo.mtgdeckmaker.adapters.DecksListener
import com.albeartwo.mtgdeckmaker.databinding.FragmentSavedDecksBinding
import com.albeartwo.mtgdeckmaker.other.AnimateFab
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

        AnimateFab.showFabWithAnimation(binding.savedDecksFab, 100)

        binding.decksListView.adapter = DecksListAdapter(DecksListener { singleDeckData ->

            findNavController().navigate(SavedDecksFragmentDirections.actionSavedDecksFragmentToDeckCardListFragment(singleDeckData.deckId))
        })

        binding.savedDecksFab.setOnClickListener {

            val input = EditText(requireContext()).apply {
                this.inputType = InputType.TYPE_CLASS_TEXT
                this.hint = "Enter Deck Name"
            }

            AlertDialog.Builder(requireContext())
                .setView(input)
                .setTitle("New Deck")
                .setPositiveButton("CREATE") { _ , _ ->

                    viewModel.insertDeck(input.text.toString())
                }
                .setNegativeButton("CANCEL" , null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }

        return binding.root
    }
}
