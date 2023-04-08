package space.mel.tutorschedule.di.modules

import org.koin.dsl.module
import space.mel.tutorschedule.repository.UserRepository

val dataModule = module {
    single { UserRepository(get(),get())}
}