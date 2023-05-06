package space.mel.tutorschedule.fragments.lesson

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.AddLessonFragmentBlackBinding
import space.mel.tutorschedule.model.Lesson
import space.mel.tutorschedule.utils.DateTimeHelper
import space.mel.tutorschedule.utils.stringCapitalize
import space.mel.tutorschedule.viewmodel.AddLessonViewModel
import space.mel.tutorschedule.viewmodel.UserViewModel
import java.util.*

class AddLessonFragment : Fragment() {
    private var _binding: AddLessonFragmentBlackBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by activityViewModel<UserViewModel>()
    private val addLessonViewModel by viewModel<AddLessonViewModel>()

    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddLessonFragmentBlackBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initClickListeners()
        initObservers()
    }

    private fun initClickListeners() {
        with(binding) {
            //TODO: переделать логику вьюмоделей
            btnOk.setOnClickListener {
                val currentLessonTimeAndDate =
                    addLessonViewModel.currentLessonTimeAndDateLiveData.value
                setLessonToCalendarIntent(currentLessonTimeAndDate)
                val lesson = Lesson(
                    dataOfLesson = currentLessonTimeAndDate,
                    userId = userViewModel.currentUserEditable.value?.let { user ->
                        listOf(user.id)
                    }
                )
                userViewModel.addLesson(lesson)
            }
            btnSelectLessonDateAndTime.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    R.style.datePickerTheme,
                    getDateListener(getTimeListener()),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).run {
                    setCancelable(false)
                    show()
                }
            }
            btnCancel.setOnClickListener {
                goBack()
            }
            btnBack.setOnClickListener {
                goBack()
            }
        }
    }

    private fun goBack() {
        findNavController().navigate(R.id.action_addLesson_to_userFullInformation)
    }

    private fun setLessonToCalendarIntent(currentLessonTimeAndDate: Long?) {
        val title = userViewModel.currentUserEditable.value?.name
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.Events.TITLE, title)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, currentLessonTimeAndDate)
            .putExtra(
                CalendarContract.EXTRA_EVENT_END_TIME,
                CalendarContract.EXTRA_EVENT_BEGIN_TIME + 60 * 60 * 1000
            )
        startActivity(intent)
    }

    private fun initObservers() {
        addLessonViewModel.dateAndTime.observe(viewLifecycleOwner) { dataAndTime ->
            binding.btnSelectLessonDateAndTime.text = stringCapitalize(dataAndTime)
            Log.d("displayFormattedDate", dataAndTime)
        }
        addLessonViewModel.currentLessonTimeAndDateLiveData.observe(viewLifecycleOwner) { date ->
            displayFormattedDate(date)
        }
        userViewModel.currentUserEditable.observe(viewLifecycleOwner) { user ->
            with(binding) {
                val grade = getString(R.string.common_grade)
                tvUserInfo.text = "${user.name + "  " + user.grade + " " + grade}"
                btnBack.text = user.name
            }
        }
    }


    private fun displayFormattedDate(timestamp: Long) {
        val dayOfWeek = DateTimeHelper.getDayOfWeek(timestamp, true)
        val dayMonthYear = DateTimeHelper.getDayMonthYear(timestamp)
        val time = DateTimeHelper.getTimeHoursMinutes(timestamp)
        val at = getString(R.string.add_lesson_fragment_tv_at)
        val fullDateOfLesson = getString(
            R.string.add_lesson_fragment_tv_formatter,
            dayOfWeek,
            dayMonthYear,
            at,
            time
        )
        addLessonViewModel.setDateAndTime(fullDateOfLesson)
        binding.btnOk.isEnabled = true
    }

    private fun getDateListener(timeListener: OnTimeSetListener): OnDateSetListener {
        val dateListener = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(year, monthOfYear, dayOfMonth)
            TimePickerDialog(
                requireContext(),
                R.style.timePickerTheme,
                timeListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).run {
                setCancelable(false)
                show()
            }
        }
        return dateListener
    }

    private fun getTimeListener(): OnTimeSetListener {
        val timeListener = OnTimeSetListener { _, hourOfDay, minute ->
            calendar.apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }
            addLessonViewModel.onDateAndTimeSetChanges(calendar.timeInMillis)
        }
        return timeListener
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}