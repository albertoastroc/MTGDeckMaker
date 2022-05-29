package com.albeartwo.mtgdeckmaker.fragments

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.albeartwo.mtgdeckmaker.R
import com.albeartwo.mtgdeckmaker.databinding.FragmentDisplayCardBinding
import com.albeartwo.mtgdeckmaker.other.AnimateFab
import com.albeartwo.mtgdeckmaker.viewmodels.DisplayCardInfoViewModel
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
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
            "results" -> AnimateFab.showFabWithAnimation(binding.saveCardButton, 100)
        }

        binding.saveCardButton.setOnClickListener {

            binding.saveCardButton.setImageResource(android.R.drawable.ic_btn_speak_now)
            YoYo.with(Techniques.FlipInX)
                .duration(700)
                .playOn(it)
            viewModel.saveCard()
        }

        return binding.root
    }



    override fun onDestroy() {
        super.onDestroy()
        viewModel._singleCardData.value = null
    }
}