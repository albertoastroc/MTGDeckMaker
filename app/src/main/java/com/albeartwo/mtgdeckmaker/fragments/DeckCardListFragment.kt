package com.albeartwo.mtgdeckmaker.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.albeartwo.mtgdeckmaker.R
import com.albeartwo.mtgdeckmaker.adapters.DeckCardListAdapter
import com.albeartwo.mtgdeckmaker.databinding.FragmentDeckCardListBinding
import com.albeartwo.mtgdeckmaker.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class DeckCardListFragment : Fragment() {

    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {

        val binding = FragmentDeckCardListBinding.inflate(inflater)

        binding.viewModel = sharedViewModel
        Timber.d("${sharedViewModel.deckId}")

        binding.lifecycleOwner = viewLifecycleOwner

        //Prevents listItem from blinking when Card.cardCount is updated
        binding.deckCardsListView.itemAnimator = null

        binding.deckCardsListView.adapter = (DeckCardListAdapter { card , action ->


            sharedViewModel._singleCard.value = card

            when (action) {

                "plus_one" -> sharedViewModel.cardQuantAddOne(card)
                "minus_one" -> {
                    if (card.cardCount == 1) {
                        sharedViewModel.removeFromDatabase(card)
                    } else {
                        sharedViewModel.cardQuantMinusOne(card)
                    }
                }
                "root" -> findNavController().navigate(
                    DeckCardListFragmentDirections.actionDeckCardListFragmentToDisplayCardInfoFragment(
                        card.cardName ,
                        "deck_card_list"
                    )
                )
            }
        })

        //Opens fragment that accepts a search query
        binding.deckCardListFab.setOnClickListener {

            findNavController().navigate(
                DeckCardListFragmentDirections.actionDeckCardListFragmentToResultList()
            )
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

                        findNavController().navigate(DeckCardListFragmentDirections.actionDeckCardListFragmentToEditDeckFragment(sharedViewModel.deckId))

                        true
                    }
                    else                  -> false
                }
            }
        } , viewLifecycleOwner , Lifecycle.State.RESUMED)
    }
}