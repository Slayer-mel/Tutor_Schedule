package space.mel.tutorschedule.fragments.lesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.UpdateLessonFragmentBlackBinding
import space.mel.tutorschedule.viewmodel.UserViewModel

class UpdateLessonFragment: Fragment() {

    private var _binding: UpdateLessonFragmentBlackBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by activityViewModel<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UpdateLessonFragmentBlackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        //initObservers()
    }

    /*private fun initObservers() {
        TODO("Not yet implemented")
    }*/

    private fun initListeners() {
        with(binding){
            btnOk.setOnClickListener {
                //updateItem()
                Toast.makeText(requireContext(),
                    R.string.update_lesson_fragment_lesson_updated,
                    Toast.LENGTH_SHORT).show()
            }
            btnBack.setOnClickListener {
                goBack()
            }
            btnCancel.setOnClickListener {
                goBack()
            }
        }
    }

    private fun goBack() {
        findNavController().navigate(R.id.action_updateLessonFragment_to_lessonFullInformation)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}