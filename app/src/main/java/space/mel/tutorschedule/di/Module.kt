package space.mel.tutorschedule.di

import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module
import space.mel.tutorschedule.data.DATABASE_NAME
import space.mel.tutorschedule.data.UserDatabase
import space.mel.tutorschedule.repository.UserRepository
import space.mel.tutorschedule.viewmodel.AddLessonViewModel
import space.mel.tutorschedule.viewmodel.UserFullInformationViewModel
import space.mel.tutorschedule.viewmodel.UserViewModel

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            databaseModule,
            repositoryModule,
            viewModelModule
           )
    )
}

val databaseModule = module {
    single { Room.databaseBuilder(get(), UserDatabase::class.java, DATABASE_NAME,).build()}
    single { get<UserDatabase>().userDao() }
    single { get<UserDatabase>().lessonDao() }
}

val repositoryModule = module {
    single { UserRepository(get(),get()) }
}

val viewModelModule = module {
    viewModel { UserViewModel(get()) }
    viewModel { UserFullInformationViewModel() }
    viewModel { AddLessonViewModel() }
}