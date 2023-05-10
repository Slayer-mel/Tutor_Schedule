package space.mel.tutorschedule.fragments.lesson

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.LessonFullInformationFragmentBlackBinding
import space.mel.tutorschedule.utils.AlertDialogProvider
import space.mel.tutorschedule.viewmodel.LessonViewModel
import space.mel.tutorschedule.viewmodel.UserViewModel


class LessonFullInformationFragment : Fragment() {
    private var _binding: LessonFullInformationFragmentBlackBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by activityViewModel<UserViewModel>()
    private val lessonViewModel by activityViewModel<LessonViewModel>()
    private var alertDeleteDialog: AlertDialog? = null

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
        alertDeleteDialog = createAlertDeleteDialog()
    }

    private fun createAlertDeleteDialog(): AlertDialog {
        return AlertDialogProvider.createAlertInstance(
            requireContext(),
            R.layout.delete_lesson_alert_dialog,
            onDelete = {
                showDeleteToast()
                deleteLesson()
            }
        )
    }


    private fun initListeners() {
        val lessonCurrent = lessonViewModel.currentLessonEditable.value
        with(binding) {
            btnEditLesson.setOnClickListener {
                lessonCurrent?.let {
                    lessonViewModel.setCurrentLessonEditable(lessonCurrent)
                    findNavController().navigate(R.id.action_lessonFullInformation_to_updateLessonFragment)
                }
            }

            btnDeleteLesson.setOnClickListener {
                alertDeleteDialog?.show()
            }
            btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_lessonFullInformation_to_listLessonFragment)
            }
        }
    }

    private fun showDeleteToast() {
        Toast.makeText(
            requireContext(),
            R.string.common_snack_bar_lesson_delete,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun deleteLesson() {
        val lesson = lessonViewModel.currentLessonEditable.value
        findNavController().navigate(R.id.action_lessonFullInformation_to_listLessonFragment)
        lesson?.let { lessonViewModel.deleteLesson(it) }
    }


    private fun initObservers() {
        lessonViewModel.currentLessonEditable.observe(viewLifecycleOwner) { lesson ->
            with(binding) {
                /*tvName.text = user.name
                tvGrade.text = user.grade.toString()+" класс"
                btnMakeCall.text = user.phonePupilNumber*/
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}