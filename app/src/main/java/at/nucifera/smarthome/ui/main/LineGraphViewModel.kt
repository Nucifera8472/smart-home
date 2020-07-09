package at.nucifera.smarthome.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*


class LineGraphViewModel : ViewModel() {

    val debugText = MutableLiveData<String>()

    val rawData = listOf(
        "2020-07-08T16:46:36.449855+00:00" to "25.7",
        "2020-07-08T19:08:44.527722+00:00" to "25.6",
        "2020-07-08T20:21:20.312123+00:00" to "25.0",
        "2020-07-08T20:25:01.363824+00:00" to "24.9",
        "2020-07-08T20:32:53.737208+00:00" to "24.5",
        "2020-07-08T20:54:38.393647+00:00" to "24.2",
        "2020-07-08T21:16:33.140235+00:00" to "24.5",
        "2020-07-08T21:33:37.267726+00:00" to "24.7",
        "2020-07-08T22:13:55.559147+00:00" to "24.8",
        "2020-07-08T22:16:36.420047+00:00" to "24.7",
        "2020-07-08T22:43:52.739038+00:00" to "24.3",
        "2020-07-08T23:15:29.400746+00:00" to "24.0",
        "2020-07-09T00:14:32.105770+00:00" to "24.4",
        "2020-07-09T00:41:28.242783+00:00" to "24.6",
        "2020-07-09T02:05:16.127215+00:00" to "24.8",
        "2020-07-09T02:59:08.194223+00:00" to "24.9",
        "2020-07-09T03:18:32.741795+00:00" to "24.8",
        "2020-07-09T04:25:57.425902+00:00" to "24.9",
        "2020-07-09T04:48:22.154936+00:00" to "24.8",
        "2020-07-09T05:10:17.206557+00:00" to "24.9",
        "2020-07-09T05:38:43.218781+00:00" to "24.8",
        "2020-07-09T06:10:40.302130+00:00" to "24.9",
        "2020-07-09T07:28:26.764351+00:00" to "25.0",
        "2020-07-09T08:06:14.921074+00:00" to "24.9",
        "2020-07-09T08:26:19.218308+00:00" to "24.8",
        "2020-07-09T09:21:21.300024+00:00" to "24.7",
        "2020-07-09T10:16:03.260296+00:00" to "24.8",
        "2020-07-09T10:46:09.967891+00:00" to "24.9",
        "2020-07-09T11:10:45.216277+00:00" to "25.2",
        "2020-07-09T11:49:13.720912+00:00" to "25.6",
        "2020-07-09T12:38:44.712091+00:00" to "25.7",
        "2020-07-09T13:04:30.029901+00:00" to "25.9",
        "2020-07-09T16:00:59.071514+00:00" to "26.0",
        "2020-07-09T16:41:57.380908+00:00" to "26.1"
    )

    fun prepareRawData() {

        try {


//            val local: DateTimeZone = dt.getZone()
//            val tz2: TimeZone = TimeZone.getDefault()
//            val localdt =
//                DateTime(dt, DateTimeZone.forID(tz2.getID()))

            val df: DateTimeFormatter = DateTimeFormat.forPattern(ISO_8601_DATE_FORMAT)
            val dateTime: DateTime =
                df.withOffsetParsed().parseDateTime("2020-07-08T16:46:36.449855+00:00")


            val localTimeZoneOffsetToUtc =
                Calendar.getInstance().timeZone.getOffset(dateTime.millis)


            debugText.value =
                dateTime.withZone(DateTimeZone.forOffsetMillis(localTimeZoneOffsetToUtc)).toString()


//            debugText.value = dateTime.chronology.toString()
//            debugText.value = dateTime.toLocalDateTime().toString()


        } catch (e: Exception) {
            debugText.value = e.message
        }


    }

    fun convertTimeZones(
        fromTimeZoneString: String?,
        toTimeZoneString: String?, fromDateTime: String?
    ): String? {
        val fromTimeZone = DateTimeZone.forID(fromTimeZoneString)
        val toTimeZone = DateTimeZone.forID(toTimeZoneString)
        val dateTime = DateTime(fromDateTime, fromTimeZone)
        val outputFormatter =
            DateTimeFormat.forPattern("yyyy-MM-dd H:mm:ss").withZone(toTimeZone)
        return outputFormatter.print(dateTime)
    }

    companion object {
        const val ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
    }

}
