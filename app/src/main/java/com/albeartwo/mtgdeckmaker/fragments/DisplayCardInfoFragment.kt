package com.albeartwo.mtgdeckmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.albeartwo.mtgdeckmaker.databinding.FragmentDisplayCardBinding
import com.albeartwo.mtgdeckmaker.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DisplayCardInfoFragment : Fragment() {

    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {

        val binding = FragmentDisplayCardBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = sharedViewModel

        Timber.d("${sharedViewModel.deckId}")

        //Checks what fragment was used to navigate here

        val args = DisplayCardInfoFragmentArgs.fromBundle(requireArguments())
        val cardName = args.cardName
        val navigatedFrom = args.fragmentName

        sharedViewModel.getSingleCardData(cardName)

        when (navigatedFrom) {

            "deck_card_list" -> binding.saveCardButton.visibility = View.INVISIBLE
        }

        binding.saveCardButton.setOnClickListener {

            sharedViewModel.saveCard()
        }

        return binding.root
    }



    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel._singleCardData.value = null
    }
}