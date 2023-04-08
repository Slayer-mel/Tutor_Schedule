package space.mel.tutorschedule.repository

import kotlinx.coroutines.flow.Flow
import space.mel.tutorschedule.data.LessonDao
import space.mel.tutorschedule.data.UserDao
import space.mel.tutorschedule.model.Lesson
import space.mel.tutorschedule.model.User

class UserRepository(
    private val userDao: UserDao,
    private val lessonDao: LessonDao
    ){

    suspend fun addLesson(lesson: Lesson) {
        lessonDao.addLesson(lesson)
    }

    suspend fun deleteLesson(lesson: Lesson) {
        lessonDao.deleteLesson(lesson)
    }



    suspend fun readAllData(): List<User> = userDao.readAllData()

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }

    fun searchDatabase(searchQuery: String): Flow<List<User>> {
        return userDao.searchDatabase(searchQuery)
    }

    fun getUser(): Flow<List<User>> = userDao.getUser()

}