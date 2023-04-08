package space.mel.tutorschedule.di.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import space.mel.tutorschedule.viewmodel.AddLessonViewModel
import space.mel.tutorschedule.viewmodel.UserFullInformationViewModel
import space.mel.tutorschedule.viewmodel.UserViewModel

val viewModelModule = module {
    viewModel { UserViewModel(get()) }
    viewModel { UserFullInformationViewModel() }
    viewModel { AddLessonViewModel() }
}