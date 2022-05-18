package com.albeartwo.mtgdeckmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.albeartwo.mtgdeckmaker.databinding.FragmentDisplayCardBinding
import com.albeartwo.mtgdeckmaker.viewmodels.SharedViewModel

class DisplayCardInfoFragment : Fragment() {

    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {

        val binding = FragmentDisplayCardBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = sharedViewModel

        //Checks what fragment was used to navigate here

        val args = DisplayCardInfoFragmentArgs.fromBundle(requireArguments())
        val cardName = args.cardName
        val navigatedFrom = args.fragmentName

        sharedViewModel.getSingleCardData(cardName)

        when (navigatedFrom) {

            "deckCardList" -> binding.saveCardButton.visibility = View.INVISIBLE
        }

        binding.saveCardButton.setOnClickListener {

            sharedViewModel.saveCard()

            findNavController().navigate(

                DisplayCardInfoFragmentDirections.actionDisplayCardInfoFragmentToDeckCardListFragment(
                    sharedViewModel.currentDeckId
                )
            )
        }

        return binding.root
    }



    override fun onDestroy() {
        super.onDestroy()
//        sharedViewModel._singleCardData.value = null
    }
}