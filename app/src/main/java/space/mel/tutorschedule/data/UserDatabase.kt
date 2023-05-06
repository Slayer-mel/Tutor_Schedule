package space.mel.tutorschedule.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import space.mel.tutorschedule.model.Lesson
import space.mel.tutorschedule.model.ParentTypeConverter
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.model.UserIdTypeConverter



@Database(
    entities = [
        User::class,
        Lesson::class
    ],
    version = 7,
    exportSchema = false
)

@TypeConverters(
    ParentTypeConverter::class,
    UserIdTypeConverter::class
)

abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun lessonDao(): LessonDao

}