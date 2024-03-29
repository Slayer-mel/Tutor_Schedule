package space.mel.tutorschedule.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import space.mel.tutorschedule.model.Lesson
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.repository.UserRepository

class UserViewModel(
    private val repository: UserRepository,
) : ViewModel() {

    val currentUserEditable : MutableLiveData<User> = MutableLiveData()
    val currentLessonEditable : MutableLiveData<Lesson> = MutableLiveData()

    fun setCurrentUserEditable (user: User){
        currentUserEditable.value = user
    }

    fun setCurrentLessonEditable (lesson: Lesson){
        currentLessonEditable.value = lesson
    }

    val users = repository.getUser().asLiveData()
    val lessons = repository.getLesson().asLiveData()

    private val userEventChannel = Channel<UserEvent>()
    val userEvent = userEventChannel.receiveAsFlow()

    private val lessonEventChannel = Channel<LessonEvent>()
    val lessonEvent = lessonEventChannel.receiveAsFlow()


    fun addLesson(lesson: Lesson){
        viewModelScope.launch(Dispatchers.IO){
            repository.addLesson(lesson)
        }
    }

    suspend fun deleteLesson(lesson: Lesson){
        repository.deleteLesson(lesson)
        lessonEventChannel.send(LessonEvent.ShowUndoDeleteLessonMessage(lesson))
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

  suspend fun deleteUser(user: User) {
            repository.deleteUser(user)
            userEventChannel.send(UserEvent.ShowUndoDeleteUserMessage(user))
    }

    fun deleteAllUser() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUsers()
        }
    }

    fun searchDatabase (searchQuery: String): LiveData<List<User>>{
        return repository.searchDatabase(searchQuery).asLiveData()
    }

    sealed class UserEvent{
        data class ShowUndoDeleteUserMessage(val user: User) : UserEvent()
    }
    sealed class LessonEvent{
        data class ShowUndoDeleteLessonMessage(val lesson: Lesson) : LessonEvent()
    }
}