package space.mel.tutorschedule

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import space.mel.tutorschedule.di.modules.daoModule
import space.mel.tutorschedule.di.modules.dataModule
import space.mel.tutorschedule.di.modules.viewModelModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                viewModelModule, daoModule, dataModule
                )
            )
        }
    }
}