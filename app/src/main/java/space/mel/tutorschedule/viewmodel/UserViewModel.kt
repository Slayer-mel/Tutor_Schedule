package space.mel.tutorschedule.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.repository.UserRepository

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    val currentUserEditable : MutableLiveData<User> = MutableLiveData()

    fun setCurrentUserEditable (user: User){
        currentUserEditable.value = user
    }

    val users = userRepository.getUser().asLiveData()

    private val userEventChannel = Channel<UserEvent>()
    val userEvent = userEventChannel.receiveAsFlow()

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addUser(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.updateUser(user)
        }
    }

  fun deleteUser(user: User) {
            viewModelScope.launch { userRepository.deleteUser(user)
                userEventChannel.send(UserEvent.ShowUndoDeleteUserMessage(user))
            }
    }

    fun deleteAllUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteAllUsers()
        }
    }

    fun searchDatabase (searchQuery: String): LiveData<List<User>>{
        return userRepository.searchDatabase(searchQuery).asLiveData()
    }

    sealed class UserEvent{
        data class ShowUndoDeleteUserMessage(val user: User) : UserEvent()
    }
}