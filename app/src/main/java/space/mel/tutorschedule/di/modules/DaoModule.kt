package space.mel.tutorschedule.di.modules

import android.content.Context
import org.koin.dsl.module
import space.mel.tutorschedule.data.UserDao
import space.mel.tutorschedule.data.UserDatabase

val daoModule = module {
    single { provideDao(get()) }
}

fun provideDao(context: Context): UserDao = UserDatabase.getDatabase(context).userDao()