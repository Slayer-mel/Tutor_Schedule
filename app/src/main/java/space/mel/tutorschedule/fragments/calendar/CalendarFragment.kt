package space.mel.tutorschedule.fragments.calendar

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import space.mel.tutorschedule.databinding.CalendarFragmentBinding

class CalendarFragment: Fragment() {

private lateinit var calendarFragmentBinding: CalendarFragmentBinding
@SuppressLint("NewApi")
val today: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        calendarFragmentBinding = CalendarFragmentBinding.inflate(layoutInflater)
        return calendarFragmentBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // intent for set event in Google calendar
        /*calendarFragmentBinding.btnAddLesson.setOnClickListener {

            val intent  = Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(Events.TITLE, "")
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, System.currentTimeMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, System.currentTimeMillis() + 60*60*1000)
            startActivity(intent)
        }*/

        //Calendar like dialog v2
        /*calendarFragmentBinding.edtDate.setOnClickListener {
            showDataPickerDialog()
        }*/


        //Time Picker
        val startHour = today.get(Calendar.HOUR_OF_DAY)
        val startMinute = today.get(Calendar.MINUTE)

        calendarFragmentBinding.btnChooseTime.setOnClickListener{
            TimePickerDialog(requireContext(), { timePicker, hourOfDay, minute ->
            calendarFragmentBinding.tvTime.setText("$hourOfDay:$minute")
            },
                startHour,
                startMinute,
                true)
                .show()
        }


        //Date Picker
        val startDay =today.get(Calendar.DAY_OF_MONTH)
        val startMonth =today.get(Calendar.MONTH)
        val startYear =today.get(Calendar.YEAR)

        calendarFragmentBinding.btnChooseDate.setOnClickListener {
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                calendarFragmentBinding.tvDate.setText("$dayOfMonth. ${month+1}. $year").toString()
                Log.d("Date1", "dayOfMonth=$dayOfMonth month+1=${month+1} year=$year")
            }, startYear, startMonth, startDay ).show()
        }

    }

    //Calendar like dialog v2
   /* private fun showDataPickerDialog() {
        val datePicker = DatePickerFragment {day, month, year ->  onDateSelected(day, month, year)}
        datePicker.show(childFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        calendarFragmentBinding.edtDate.setText("$day ${month+1} $year")
    }*/


}