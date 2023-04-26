package space.mel.tutorschedule.utils

import java.util.*

fun stringCapitalize(string: String):String{
    return string.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
}