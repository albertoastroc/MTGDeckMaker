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
import com.albeartwo.mtgdeckmaker.database.Deck
import com.albeartwo.mtgdeckmaker.databinding.FragmentAddNewDeckBinding
import com.albeartwo.mtgdeckmaker.viewmodels.AddNewDeckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_new_deck.*

@AndroidEntryPoint
class AddNewDeckFragment : Fragment() {

    private val viewModel : AddNewDeckViewModel by viewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {

        val binding = FragmentAddNewDeckBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.saveNewDeckBt.setOnClickListener() {

            val deckName = newDeckNameEt.text.toString()
            val deck = Deck(deckName)
            viewModel.insertDeck(deck)

            saveNewDeckBt.findNavController().navigate(AddNewDeckFragmentDirections.actionAddNewDeckFragmentToSavedDecksFragment())
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()

        //Hide the soft keyboard when this fragment disappears
        val imm : InputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken , 0)
    }
}