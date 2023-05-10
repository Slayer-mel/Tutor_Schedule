package space.mel.tutorschedule.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateTimeHelper {

    private const val DAY_OF_WEEK = "EEEE"
    private const val DAY_MONTH_YEAR = "dd.MM.yyyy"
    private const val TIME_HOURS_MINUTES = "HH:mm"

    @SuppressLint("SimpleDateFormat")
    fun getDayOfWeek(millis: Long, isCapitalize: Boolean = false): String {
        return SimpleDateFormat(DAY_OF_WEEK).run {
            format(millis).apply {

                if (isCapitalize) {
                    replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                        else it.toString() }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDayMonthYear(millis: Long): String {
        return SimpleDateFormat(DAY_MONTH_YEAR).run {
            format(millis)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getTimeHoursMinutes(millis: Long): String {
        return SimpleDateFormat(TIME_HOURS_MINUTES).run{
            format(millis)
        }
    }
}