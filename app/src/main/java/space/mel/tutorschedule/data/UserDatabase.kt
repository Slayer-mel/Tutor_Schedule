package space.mel.tutorschedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
    version =7,
    exportSchema = false
)

@TypeConverters(
    ParentTypeConverter::class,
    UserIdTypeConverter::class
)

abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun lessonDao(): LessonDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}