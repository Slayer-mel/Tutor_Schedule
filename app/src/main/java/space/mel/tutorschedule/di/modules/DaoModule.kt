package space.mel.tutorschedule.di.modules

import android.content.Context
import org.koin.dsl.module
import space.mel.tutorschedule.data.LessonDao
import space.mel.tutorschedule.data.UserDao
import space.mel.tutorschedule.data.UserDatabase

val daoModule = module {
    single {provideUserDao(get())}
    single {provideLessonDao(get())}
}

fun provideUserDao(context: Context): UserDao = UserDatabase.getDatabase(context).userDao()
fun provideLessonDao(context: Context) : LessonDao = UserDatabase.getDatabase(context).lessonDao()