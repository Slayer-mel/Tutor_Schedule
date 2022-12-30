package space.mel.tutorschedule.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import space.mel.tutorschedule.data.UserDao
import space.mel.tutorschedule.model.User

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUsers(){
        userDao.deleteAllUsers()
    }

    fun searchDatabase (searchQuery: String): Flow<List<User>>{
        return userDao.searchDatabase(searchQuery)
    }

}