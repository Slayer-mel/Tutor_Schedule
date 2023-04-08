package space.mel.tutorschedule.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddLessonViewModel : ViewModel() {
    val dateAndTime : MutableLiveData<String> = MutableLiveData()

    fun setDateAndTime(dateValue:String){
        dateAndTime.value = dateValue
    }
}
