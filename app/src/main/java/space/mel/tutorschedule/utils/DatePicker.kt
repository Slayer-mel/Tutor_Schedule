package space.mel.tutorschedule.utils

import android.app.DatePickerDialog
import android.widget.DatePicker
import java.util.*

val calendar: Calendar = Calendar.getInstance()
val dateSetListener = object : DatePickerDialog.OnDateSetListener {
    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                           dayOfMonth: Int) {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
    }
}


