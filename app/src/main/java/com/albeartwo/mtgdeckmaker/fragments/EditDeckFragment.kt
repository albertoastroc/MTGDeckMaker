package com.albeartwo.mtgdeckmaker.fragments

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.albeartwo.mtgdeckmaker.R
import com.albeartwo.mtgdeckmaker.database.Deck
import com.albeartwo.mtgdeckmaker.databinding.FragmentEditDeckBinding
import com.albeartwo.mtgdeckmaker.viewmodels.EditDeckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit_deck.*

@AndroidEntryPoint
class EditDeckFragment : Fragment() {

    private val viewModel : EditDeckViewModel by viewModels()


    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {

        val deckId = EditDeckFragmentArgs.fromBundle(requireArguments()).deckId

        val binding = FragmentEditDeckBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel


        //Checks if deck is being edited or new deck is being added
        if (deckId != 0){

            binding.saveButton.text = "Change Deck Name"

            binding.saveButton.setOnClickListener {

                val deckName = deckNameEditText.text.toString()
                val deck = Deck(deckName)
                viewModel.changeDeckName(deck)

                saveButton.findNavController().navigate(EditDeckFragmentDirections.actionEditDeckFragmentToSavedDecksFragment())

            }

        } else {

            binding.saveButton.text = "Save New Deck"

            binding.saveButton.setOnClickListener(){

                val deckName = deckNameEditText.text.toString()
                val deck  = Deck(deckName)
                viewModel.insertDeck(deck)


                saveButton.findNavController().navigate(EditDeckFragmentDirections.actionEditDeckFragmentToSavedDecksFragment())

            }

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