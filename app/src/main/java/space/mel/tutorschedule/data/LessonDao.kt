package space.mel.tutorschedule.data

import androidx.room.*
import space.mel.tutorschedule.model.Lesson
import space.mel.tutorschedule.model.relations.UserWithLessons

@Dao
interface LessonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLesson(lesson: Lesson)

    @Delete
    suspend fun deleteLesson(lesson: Lesson)

    @Transaction
    @Query("SELECT * FROM user_table WHERE id= :id")
    suspend fun getUserWithLessons(id: String): List<UserWithLessons>
}