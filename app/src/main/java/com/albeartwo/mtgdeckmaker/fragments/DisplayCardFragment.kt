import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.albeartwo.mtgdeckmaker.databinding.FragmentDisplayCardBinding
import com.albeartwo.mtgdeckmaker.fragments.DisplayCardFragmentArgs
import com.albeartwo.mtgdeckmaker.fragments.DisplayCardFragmentDirections
import com.albeartwo.mtgdeckmaker.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisplayCardFragment : Fragment() {

    private val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle?
    ) : View {

        val binding = FragmentDisplayCardBinding.inflate(inflater)

        binding.lifecycleOwner = this
        
        binding.viewModel = sharedViewModel

        val args = DisplayCardFragmentArgs.fromBundle(requireArguments()).fromFragment

        //Checks what fragment was used to navigate here
        when (args) {

            "results" -> {}
            else      -> {

                sharedViewModel.getSingleCardData(args)
                binding.saveCardButton.visibility = View.INVISIBLE
            }
        }

        binding.saveCardButton.setOnClickListener {

            sharedViewModel.saveCard()

            it.findNavController().navigate(

                DisplayCardFragmentDirections.actionDisplayCardFragmentToDeckCardListFragment(
                    sharedViewModel.currentDeckId
                )
            )
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel._singleCardData.value = null
    }
}