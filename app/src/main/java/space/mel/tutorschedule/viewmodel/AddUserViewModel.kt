package space.mel.tutorschedule.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.repository.UserRepository

class AddUserViewModel(
    private val userRepository: UserRepository
    ):ViewModel() {


    private val name : MutableLiveData<String> = MutableLiveData()
    private val grade : MutableLiveData<String> = MutableLiveData()

    fun setName(nameValue:String){
        name.value = nameValue
    }
    fun setGrade(gradeValue:String){
        grade.value = gradeValue
    }

    val isButtonOkValid : MutableLiveData<Boolean> = MutableLiveData()

    fun isFieldsValid(){
        isButtonOkValid.value = !(name.value.isNullOrEmpty() || grade.value.isNullOrEmpty())
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.addUser(user)
        }
    }
}