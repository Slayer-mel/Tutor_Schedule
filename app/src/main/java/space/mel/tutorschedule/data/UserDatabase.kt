package space.mel.tutorschedule.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import space.mel.tutorschedule.model.Lesson
import space.mel.tutorschedule.model.ParentTypeConverter
import space.mel.tutorschedule.model.User
import space.mel.tutorschedule.model.UserIdTypeConverter

const val DATABASE_NAME = "user_database"

@Database(
    entities = [
        User::class,
        Lesson::class
    ],
    //TODO: Нахрена тебе аж седьмая версия БД? У тебя ни одной миграции нет
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