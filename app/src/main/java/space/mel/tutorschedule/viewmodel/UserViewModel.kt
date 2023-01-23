package space.mel.tutorschedule.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.repository.UserRepository

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    val readAllData: MutableLiveData<List<User>> = MutableLiveData<List<User>>()
    //private val repository: UserRepository = UserRepository(userDao)

    init {
        //val userDao = UserDatabase.getDatabase(application).userDao()
        //repository = UserRepository(userDao)
        //readAllData = repository.readAllData
    }
    fun fetchAllData(){
        viewModelScope.launch(Dispatchers.IO) {
            readAllData.postValue(repository.readAllData())
        }
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

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUser(user)
        }
    }

    fun deleteAllUser() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllUsers()
        }
    }

    fun searchDatabase (searchQuery: String): LiveData<List<User>>{
        return repository.searchDatabase(searchQuery).asLiveData()
    }
}