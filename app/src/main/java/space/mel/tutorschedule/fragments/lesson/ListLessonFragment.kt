package space.mel.tutorschedule.fragments.lesson

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.ListLessonFragmentBlackBinding
import space.mel.tutorschedule.model.Lesson
import space.mel.tutorschedule.utils.Constants
import space.mel.tutorschedule.utils.SwipeHelper
import space.mel.tutorschedule.viewmodel.LessonViewModel
import space.mel.tutorschedule.viewmodel.UserViewModel

class ListLessonFragment : Fragment() {
    private var _binding: ListLessonFragmentBlackBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by activityViewModel<UserViewModel>()
    private val lessonViewModel by activityViewModel<LessonViewModel>()
    private val listLessonAdapter: ListLessonAdapter by lazy { ListLessonAdapter(::startFullInformation) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListLessonFragmentBlackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListeners()
        initAdapter()
        initObservers()
    }

    private fun initObservers() {
        // UserViewModel
        lessonViewModel.lessons.observe(viewLifecycleOwner) { lessonList ->
            val userId = userViewModel.currentUserEditable.value!!.id

            val filteredListOfLesson = lessonList.filter {
                it.userId?.contains(userId) == true
            }
            val sortedListOfLesson = filteredListOfLesson.sortedByDescending { it.dataOfLesson }
            listLessonAdapter.submitList(sortedListOfLesson)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            lessonViewModel.lessonEvent.collect { event ->
                if (event is LessonViewModel.LessonEvent.ShowUndoDeleteLessonMessage) {
                    Snackbar.make(
                        requireView(),
                        R.string.common_snack_bar_lesson_delete,
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(R.string.common_btn_cancel_gray_background) {
                            lessonViewModel.addLesson(event.lesson)
                        }
                        .setDuration(Constants.DURATION_TIME_DELETE_MESSAGE)
                        .setActionTextColor(Color.RED)
                        .show()
                }
            }
        }
    }

    private fun initListeners() {
        with(binding) {
            btnBack.setOnClickListener {
                findNavController().navigate(R.id.action_listLessonFragment_to_userFullInformation)
            }
        }
    }

    private fun initAdapter() {
        binding.apply {
            recyclerview.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = listLessonAdapter
                setHasFixedSize(true)
            }
            //swipe delete item from Room DB
            ItemTouchHelper(
                SwipeHelper { viewHolderAdapterPosition ->
                    val lesson = listLessonAdapter.currentList[viewHolderAdapterPosition]
                    lifecycleScope.launch {
                        lessonViewModel.deleteLesson(lesson)
                    }
                }
            ).attachToRecyclerView(recyclerview)
        }
    }

    private fun startFullInformation(lesson: Lesson) {
        lessonViewModel.currentLessonEditable.value = lesson
        findNavController().navigate(R.id.action_listLessonFragment_to_lessonFullInformation)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
