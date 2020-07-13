package at.nucifera.smarthome.extensions

import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by blexa, 13.07.2020.
 */
class DateTimeKtTest {

    @Test
    fun roundToMinutes_roundUp() {
        val dateTime = DateTime.parse("2020-07-09T05:32:30.218781")
        val rounded = dateTime.roundToMinutes(5)
        assertEquals(5, rounded.hourOfDay)
        assertEquals(35, rounded.minuteOfHour)
        assertEquals(0, rounded.secondOfMinute)
        assertEquals(0, rounded.millisOfSecond)
    }

    @Test
    fun roundToMinutes_roundDown() {
        val dateTime = DateTime.parse("2020-07-09T05:32:29.218781")
        val rounded = dateTime.roundToMinutes(5)
        assertEquals(5, rounded.hourOfDay)
        assertEquals(30, rounded.minuteOfHour)
        assertEquals(0, rounded.secondOfMinute)
        assertEquals(0, rounded.millisOfSecond)
    }

}
