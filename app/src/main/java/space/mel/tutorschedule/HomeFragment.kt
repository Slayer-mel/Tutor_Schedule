package space.mel.tutorschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import space.mel.tutorschedule.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {
    lateinit var homeBinding: HomeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = HomeFragmentBinding.inflate(layoutInflater)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeBinding.btnGoOn.setOnClickListener{
        findNavController().navigate(R.id.action_homeFragment_to_pupilsListFragment)
        }
    }
}