package at.nucifera.smarthome

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber

import timber.log.Timber.DebugTree


/**
 * Created by blexa, 09.07.2020.
 */
class SmartHomeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
//            Timber.plant(CrashReportingTree())
        }
    }
}
