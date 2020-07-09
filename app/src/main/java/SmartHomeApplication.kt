import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by blexa, 09.07.2020.
 */
class SmartHomeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}
