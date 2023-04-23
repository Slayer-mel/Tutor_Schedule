package space.mel.tutorschedule.fragments.lesson

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.LessonFullInformationFragmentBlackBinding
import space.mel.tutorschedule.viewmodel.UserViewModel

class LessonFullInformation : Fragment() {
    private var _binding: LessonFullInformationFragmentBlackBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by activityViewModel<UserViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LessonFullInformationFragmentBlackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        initObservers()
    }


    private fun initListeners() {
        val lessonCurrent = userViewModel.currentLessonEditable.value
        with(binding) {
            btnEditLesson.setOnClickListener {
                if (lessonCurrent != null) {
                    userViewModel.setCurrentLessonEditable(lessonCurrent)
                    findNavController().navigate(R.id.action_lessonFullInformation_to_updateLessonFragment)
                }
            }

            btnDeleteLesson.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext()).create()
                val view = View.inflate(requireContext(), R.layout.delete_lesson_alert_dialog, null)
                val btnCancel = view.findViewById<TextView>(R.id.btnCancel)
                val btnDelete = view.findViewById<TextView>(R.id.btnDelete)
                btnCancel.setOnClickListener {
                    builder.dismiss()
                }
                btnDelete.setOnClickListener {
                    Toast.makeText(
                        requireContext(), "Урок удален",
                        Toast.LENGTH_LONG
                    ).show()
                    lifecycleScope.launch {
                        deleteLesson()
                    }
                    builder.dismiss()
                }
                with(builder) {
                    setView(view)
                    setCancelable(false)
                    show()
                }
            }


            btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_lessonFullInformation_to_listLessonFragment)
            }
        }
    }

    private suspend fun deleteLesson() {
        val lesson = userViewModel.currentLessonEditable.value
        findNavController().navigate(R.id.action_userFullInformation_to_listFragmentBlack)
        lesson?.let { userViewModel.deleteLesson(it) }
    }


    private fun initObservers() {
        userViewModel.currentLessonEditable.observe(viewLifecycleOwner) {lesson->
            with(binding) {
                /*tvName.text = user.name
                tvGrade.text = user.grade.toString()+" класс"
                btnMakeCall.text = user.phonePupilNumber*/
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}