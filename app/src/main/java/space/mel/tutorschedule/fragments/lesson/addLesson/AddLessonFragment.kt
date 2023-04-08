package space.mel.tutorschedule.fragments.lesson.addLesson

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import space.mel.tutorschedule.R
import space.mel.tutorschedule.databinding.AddLessonFragmentBlackBinding
import space.mel.tutorschedule.model.Lesson
import space.mel.tutorschedule.viewmodel.AddLessonViewModel
import space.mel.tutorschedule.viewmodel.UserViewModel
import java.text.SimpleDateFormat

class AddLessonFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private var _binding: AddLessonFragmentBlackBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by activityViewModel<UserViewModel>()
    private val addLessonViewModel by viewModel<AddLessonViewModel>()
    @RequiresApi(Build.VERSION_CODES.N)
    private val calendar = Calendar.getInstance()
    private var currentLessonTimeAndData: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddLessonFragmentBlackBinding.inflate(layoutInflater)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            binding.btnChooseLessonDateAndTime.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, 0)
            val dialog=DatePickerDialog(
                requireContext(),
                R.style.datePickerTheme,
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            //минимальная дата начала занятий с сегодняшнего дня
            //dialog.datePicker.minDate = calendar.timeInMillis
                with(dialog){
                    setCancelable(false)
                    show()
                }
        }
        initClickListeners()
        initObservers()
    }

    private fun initClickListeners() {
        with(binding){
            //add Lesson to Calendar
                btnOk.setOnClickListener{
                    if (btnChooseLessonDateAndTime.text.isNotEmpty()){
                        val title = userViewModel.currentUserEditable.value?.name
                        val intent  = Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.Events.TITLE, title)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, currentLessonTimeAndData)
                            .putExtra(
                                CalendarContract.EXTRA_EVENT_END_TIME,
                                CalendarContract.EXTRA_EVENT_BEGIN_TIME + 60*60*1000)
                        startActivity(intent)
                        val lesson= Lesson(
                            dataOfLesson = currentLessonTimeAndData
                        )
                        userViewModel.addLesson(lesson)
                    }else {
                        Toast.makeText(requireContext(), "Выбирите дату", Toast.LENGTH_SHORT).show()
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

    private fun initObservers() {
        addLessonViewModel.dateAndTime.observe(viewLifecycleOwner){ dataAndTime->
            binding.btnChooseLessonDateAndTime.text = dataAndTime
            Log.d("displayFormattedDate", dataAndTime)
        }
        userViewModel.currentUserEditable.observe(viewLifecycleOwner){user->
            with(binding){
                val g = getString(R.string.common_grade)
                tvUserInfo.text = "${user.name+ "  " + user.grade+ " " + g}"
                btnBack.text = user.name
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        val timePicker = TimePickerDialog(
            requireContext(),
            R.style.timePickerTheme,
            this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        with(timePicker){
            setCancelable(false)
            show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
        displayFormattedDate(calendar.timeInMillis)
    }

    private fun displayFormattedDate(timestamp: Long){
        val at = getString(R.string.add_lesson_fragment_tv_at)
        val datePattern = getString(R.string.add_lesson_fragment_tv_formatter, at)
        @SuppressLint("SimpleDateFormat")
        val formatter = SimpleDateFormat(datePattern)
        val fullDateOfLesson = formatter.format(timestamp)
        addLessonViewModel.setDateAndTime(fullDateOfLesson)
        currentLessonTimeAndData = formatter.parse(fullDateOfLesson)?.time
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}