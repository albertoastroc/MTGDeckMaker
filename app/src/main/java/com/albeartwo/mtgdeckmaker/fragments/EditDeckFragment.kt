package com.albeartwo.mtgdeckmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
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

        val binding = FragmentEditDeckBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.saveButton.setOnClickListener(){

            val deckName = deckNameEditText.text.toString()
            val deck  = Deck(0, deckName)
            viewModel.insertDeck(deck)

            saveButton.findNavController().navigate(EditDeckFragmentDirections.actionEditDeckFragmentToSavedDecksFragment())

        }


        return binding.root

    }





}