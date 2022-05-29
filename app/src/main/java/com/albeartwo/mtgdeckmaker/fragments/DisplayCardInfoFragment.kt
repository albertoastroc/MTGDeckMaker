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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_display_card.*

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
            "results"        -> AnimateFab.showFabWithAnimation(binding.saveCardButton , 100)
        }

        binding.saveCardButton.setOnClickListener {

            animateFab(binding.saveCardButton)
        }

        return binding.root
    }

    fun animateFab(fab : FloatingActionButton) {

        when (viewModel._inDeck.value) {
            true -> {
                YoYo.with(Techniques.FlipInX)
                    .duration(700)
                    .playOn(fab)
                fab.setImageResource(android.R.drawable.ic_input_add)
                fab.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.green, null))
                viewModel.removeFromDatabase()
            }
            false -> {
                YoYo.with(Techniques.FlipInX)
                    .duration(700)
                    .playOn(fab)
                fab.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                fab.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.red, null))
                viewModel.saveCard()
            }
            else -> {}
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel._singleCardData.value = null
    }
}