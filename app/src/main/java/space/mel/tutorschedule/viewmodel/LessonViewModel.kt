package space.mel.tutorschedule.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import space.mel.tutorschedule.model.Lesson
import space.mel.tutorschedule.repository.LessonRepository

class LessonViewModel(
    private val lessonRepository: LessonRepository
    ):ViewModel() {

    val currentLessonEditable : MutableLiveData<Lesson> = MutableLiveData()

    fun setCurrentLessonEditable (lesson: Lesson){
        currentLessonEditable.value = lesson
    }

    val lessons = lessonRepository.getLesson().asLiveData()

    private val lessonEventChannel = Channel<LessonEvent>()
    val lessonEvent = lessonEventChannel.receiveAsFlow()

    fun addLesson(lesson: Lesson){
        viewModelScope.launch(Dispatchers.IO){
            lessonRepository.addLesson(lesson)
        }
    }

    fun deleteLesson(lesson: Lesson){
        viewModelScope.launch {
            lessonRepository.deleteLesson(lesson)
            lessonEventChannel.send(LessonEvent.ShowUndoDeleteLessonMessage(lesson))
        }
    }

    sealed class LessonEvent{
        data class ShowUndoDeleteLessonMessage(val lesson: Lesson) : LessonEvent()
    }
}