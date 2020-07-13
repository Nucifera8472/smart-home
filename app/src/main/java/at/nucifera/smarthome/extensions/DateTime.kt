package at.nucifera.smarthome.extensions

import org.joda.time.DateTime
import org.joda.time.Duration
import kotlin.math.roundToInt

/**
 * Created by blexa, 13.07.2020.
 */
fun DateTime.roundToMinutes(minutes: Int): DateTime {
    require(minutes > 0) { "minutes must be > 0" }
    val hour = this.hourOfDay().roundFloorCopy()
    val millisSinceHour: Long = Duration(hour, this).millis
    val roundedMinutes = (millisSinceHour / 60000.0 / minutes).roundToInt() * minutes
    return hour.plusMinutes(roundedMinutes)
}
