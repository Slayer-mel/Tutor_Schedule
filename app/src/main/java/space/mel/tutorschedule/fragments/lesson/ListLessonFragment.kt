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
import space.mel.tutorschedule.utils.SwipeHelper
import space.mel.tutorschedule.viewmodel.UserViewModel

class ListLessonFragment : Fragment() {
    private var _binding: ListLessonFragmentBlackBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by activityViewModel<UserViewModel>()
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
    }

    private fun initListeners() {
        with(binding){
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
                SwipeHelper{ viewHolderAdapterPosition->
                    val lesson = listLessonAdapter.currentList[viewHolderAdapterPosition]
                    lifecycleScope.launch {
                        userViewModel.deleteLesson(lesson)
                    }
                }
            ).attachToRecyclerView(recyclerview)
        }

        // UserViewModel
        //TODO: Какого хуя это делает в функции "initAdapter"?
        userViewModel.lessons.observe(viewLifecycleOwner) { lessonList ->
            val userId = userViewModel.currentUserEditable.value!!.id

            val filteredListOfLesson = lessonList.filter {
                it.userId?.contains(userId) == true
            }
            val sortedListOfLesson = filteredListOfLesson.sortedByDescending { it.dataOfLesson }
            listLessonAdapter.submitList(sortedListOfLesson)
        }

        //TODO: Какого хуя это делает в функции "initAdapter"?
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            userViewModel.lessonEvent.collect { event ->
                //TODO: Тебе не нужен when, если у тебя всего одно условие. Используй if
                when (event) {
                    is UserViewModel.LessonEvent.ShowUndoDeleteLessonMessage -> {
                        Snackbar.make(
                            requireView(),
                            //TODO: В строковые ресурсы
                            "Урок удалён",
                            Snackbar.LENGTH_INDEFINITE
                        )
                            //TODO: В строковые ресурсы
                            .setAction("Отмена") {
                                userViewModel.addLesson(event.lesson)
                            }
                            //TODO: Стилистическая ошибка, которая называется "Magic number".
                            // Вынеси эту цифру в константу и назови нормально
                            .setDuration(4500)
                            .setActionTextColor(Color.RED)
                            .show()
                    }
                }
            }
        }
    }

    private fun startFullInformation(lesson: Lesson) {
        userViewModel.currentLessonEditable.value = lesson
        findNavController().navigate(R.id.action_listLessonFragment_to_lessonFullInformation)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
