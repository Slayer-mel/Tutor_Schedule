package space.mel.tutorschedule.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddUserViewModel:ViewModel() {

    private val name : MutableLiveData<String> = MutableLiveData()

    fun setName(nameValue:String){
        name.value = nameValue
    }

    private val grade : MutableLiveData<String> = MutableLiveData()

    fun setGrade(gradeValue:String){
        grade.value = gradeValue
    }
}