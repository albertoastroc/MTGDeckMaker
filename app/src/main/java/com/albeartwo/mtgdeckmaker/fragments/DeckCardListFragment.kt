package com.albeartwo.mtgdeckmaker.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

        setHasOptionsMenu(true)

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

        binding.deckCardListFab.setOnClickListener {

            if (viewModel.deckId != null){

                findNavController().navigate(
                    DeckCardListFragmentDirections.actionDeckCardListFragmentToResultList(
                        viewModel.deckId !!
                    )
                )

            }



        }

        return binding.root
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {

        if (viewModel.deckId != null){

            when (item.itemId){

                R.id.editDeckItem -> findNavController().navigate(DeckCardListFragmentDirections.actionDeckCardListFragmentToEditDeckFragment(viewModel.deckId !!))

            }

        }



        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu : Menu , inflater : MenuInflater) {
        inflater.inflate(R.menu.edit_deck_menu, menu)
    }

}