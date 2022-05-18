package com.albeartwo.mtgdeckmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.albeartwo.mtgdeckmaker.databinding.FragmentDisplayCardBinding
import com.albeartwo.mtgdeckmaker.viewmodels.DisplayCardInfoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisplayCardInfoFragment : Fragment() {

    private val viewModel : DisplayCardInfoViewModel by viewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {

        val binding = FragmentDisplayCardBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        //Checks what fragment was used to navigate here

        val args = DisplayCardInfoFragmentArgs.fromBundle(requireArguments())
        val cardName = args.cardName
        val navigatedFrom = args.fragmentName

        viewModel.getSingleCardData(cardName)

        when (navigatedFrom) {

            "deck_card_list" -> binding.saveCardButton.visibility = View.INVISIBLE
        }

        binding.saveCardButton.setOnClickListener {

            viewModel.saveCard()

            findNavController().navigate(

                DisplayCardInfoFragmentDirections.actionDisplayCardInfoFragmentToDeckCardListFragment(
                    viewModel.currentDeckId
                )
            )
        }

        return binding.root
    }



    override fun onDestroy() {
        super.onDestroy()
        viewModel._singleCardData.value = null
    }
}