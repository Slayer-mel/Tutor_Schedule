package space.mel.tutorschedule.repository

import kotlinx.coroutines.flow.Flow
import space.mel.tutorschedule.data.LessonDao
import space.mel.tutorschedule.model.Lesson

class LessonRepository(private val lessonDao: LessonDao) {
    suspend fun addLesson(lesson: Lesson) {
        lessonDao.addLesson(lesson)
    }

    suspend fun deleteLesson(lesson: Lesson) {
        lessonDao.deleteLesson(lesson)
    }

    fun getLesson(): Flow<List<Lesson>> = lessonDao.getLesson()
}