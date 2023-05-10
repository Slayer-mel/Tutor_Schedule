package space.mel.tutorschedule.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.mel.tutorschedule.model.Lesson
import space.mel.tutorschedule.repository.LessonRepository

class AddLessonViewModel(
    private val lessonRepository: LessonRepository
) : ViewModel() {
    val dateAndTime : MutableLiveData<String> = MutableLiveData()
    val currentLessonTimeAndDateLiveData = MutableLiveData<Long>()

    fun onDateAndTimeSetChanges(date: Long){
        currentLessonTimeAndDateLiveData.value = date
    }

    fun setDateAndTime(dateValue:String){
        dateAndTime.value = dateValue
    }

     fun addLesson(listOfID: List<Int>?){
        viewModelScope.launch(Dispatchers.IO){
            lessonRepository.addLesson(createLesson(listOfID))
        }
    }

     private fun createLesson(listOfID: List<Int>?): Lesson {
        return Lesson(
            dataOfLesson = currentLessonTimeAndDateLiveData.value,
            userId = listOfID
        )
    }
}
