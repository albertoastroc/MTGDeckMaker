package com.albeartwo.mtgdeckmaker.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.albeartwo.mtgdeckmaker.R
import com.albeartwo.mtgdeckmaker.adapters.DeckCardListAdapter
import com.albeartwo.mtgdeckmaker.databinding.FragmentDeckCardListBinding
import com.albeartwo.mtgdeckmaker.viewmodels.DeckCardListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeckCardListFragment : Fragment() {

    private val viewModel : DeckCardListViewModel by viewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {

        val binding = FragmentDeckCardListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //Prevents listItem from blinking when Card.cardCount is updated
        binding.deckCardsListView.itemAnimator = null
        binding.deckCardsListView.adapter = (DeckCardListAdapter { card , action ->

            when (action) {

                "plus_one"  -> viewModel.cardQuantAddOne(card)
                "minus_one" -> {
                    if (card.cardCount == 1) {
                        viewModel.removeFromDatabase(card)
                    } else {
                        viewModel.cardQuantMinusOne(card)
                    }

                }
                "root"      -> findNavController().navigate(DeckCardListFragmentDirections.actionDeckCardListFragmentToDisplayCardFragment(card.cardName))
            }
            viewModel._singleCard.value = card
        })

        //Opens fragment that searches for cards
        binding.deckCardListFab.setOnClickListener {

            if (viewModel.deckId != null) {
                findNavController().navigate(
                    DeckCardListFragmentDirections.actionDeckCardListFragmentToResultList(
                        viewModel.deckId !!
                    )
                )
            }
        }

        return binding.root
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {

        val menuHost : MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu : Menu , menuInflater : MenuInflater) {
                menuInflater.inflate(R.menu.deck_card_list_menu , menu)
            }

            override fun onMenuItemSelected(menuItem : MenuItem) : Boolean {

                return when (menuItem.itemId) {
                    R.id.deckCardListItem -> {
                        if (viewModel.deckId != null) {
                            findNavController().navigate(DeckCardListFragmentDirections.actionDeckCardListFragmentToEditDeckFragment(viewModel.deckId!!))
                        }
                        true
                    }
                    else                  -> false
                }
            }
        } , viewLifecycleOwner , Lifecycle.State.RESUMED)
    }
}