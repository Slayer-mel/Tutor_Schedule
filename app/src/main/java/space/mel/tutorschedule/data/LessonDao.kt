package space.mel.tutorschedule.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
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

    //TODO: переименуй функцию. Ты получаешь список из лессонов, а не один
    @Query("SELECT * FROM Lesson")
    fun getLesson() : Flow<List<Lesson>>
}