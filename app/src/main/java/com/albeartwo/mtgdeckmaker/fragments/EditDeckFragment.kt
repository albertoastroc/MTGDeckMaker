package com.albeartwo.mtgdeckmaker.fragments

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.albeartwo.mtgdeckmaker.R
import com.albeartwo.mtgdeckmaker.database.Deck
import com.albeartwo.mtgdeckmaker.databinding.FragmentEditDeckBinding
import com.albeartwo.mtgdeckmaker.viewmodels.EditDeckViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_edit_deck.*

@AndroidEntryPoint
class EditDeckFragment : Fragment() {

    private val viewModel : EditDeckViewModel by viewModels()
    private var deckId = 0
    private var newDeck : Boolean = true

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {

        deckId = EditDeckFragmentArgs.fromBundle(requireArguments()).deckId

        val binding = FragmentEditDeckBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //Checks if deck is being edited or new deck is being added
        if (deckId != 0) {

            newDeck = false

            binding.saveButton.text = "Change Deck Name"
            binding.saveButton.setOnClickListener {

                val deckName = deckNameEditText.text.toString()
                viewModel.changeDeckName(deckName , deckId)

                saveButton.findNavController().navigate(EditDeckFragmentDirections.actionEditDeckFragmentToSavedDecksFragment())
            }

        } else {

            newDeck = true

            binding.saveButton.text = "Save New Deck"
            binding.saveButton.setOnClickListener() {

                val deckName = deckNameEditText.text.toString()
                val deck = Deck(deckName)
                viewModel.insertDeck(deck)

                saveButton.findNavController().navigate(EditDeckFragmentDirections.actionEditDeckFragmentToSavedDecksFragment())
            }
        }

        return binding.root

    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {

        val menuHost : MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu : Menu , menuInflater : MenuInflater) {
                menuInflater.inflate(R.menu.edit_deck_menu , menu)
            }

            override fun onMenuItemSelected(menuItem : MenuItem) : Boolean {

                return when (menuItem.itemId) {
                    R.id.editDeckItem -> {

                        AlertDialog.Builder(context)
                            .setTitle("Delete deck")
                            .setMessage("Are you sure you want to delete this deck?")
                            .setPositiveButton("DELETE") { _ , _ ->
                                viewModel.deleteDeck(deckId)
                                findNavController().navigate(EditDeckFragmentDirections.actionEditDeckFragmentToSavedDecksFragment())
                            }
                            .setNegativeButton("CANCEL" , null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show()
                        true
                    }
                    else              -> {
                        false
                    }
                }
            }
        } , viewLifecycleOwner , Lifecycle.State.RESUMED)
    }

    override fun onPause() {
        super.onPause()

        //Hide the soft keyboard when this fragment disappears
        val imm : InputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken , 0)
    }
}