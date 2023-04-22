package space.mel.tutorschedule.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddLessonViewModel : ViewModel() {
    val dateAndTime : MutableLiveData<String> = MutableLiveData()
    val currentLessonTimeAndDateLiveData = MutableLiveData<Long>()

    fun onDateAndTimeSetChanges(date: Long){
        currentLessonTimeAndDateLiveData.value = date
    }

    fun setDateAndTime(dateValue:String){
        dateAndTime.value = dateValue
    }
}
