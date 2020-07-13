package at.nucifera.smarthome.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import at.nucifera.smarthome.extensions.roundToMinutes
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Interval
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import timber.log.Timber
import java.util.*


class LineGraphViewModel : ViewModel() {

    val debugText = MutableLiveData<String>()

    private val rawData = listOf(
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

    fun prepareRawData(): List<Pair<DateTime, Float?>> {

        try {

            val preparedData = rawData.map { parseDateTime(it.first) to it.second.toFloat() }

            val bucketSizeInMinutes = 5

            // we start the first bucket 2.5 minutes before the first timestamp and create time stamp buckets in 5 minute intervals until all time stamps landed in a bucket
            val roundedFirstTimeStamp =
                preparedData.first().first.roundToMinutes(bucketSizeInMinutes)


            // if the first timestamp is rounded up, count down 5 minutes to start the first bucket to not miss the first time stamp
            val firstBucket =
                (if (roundedFirstTimeStamp.minusSeconds(150).isAfter(preparedData.first().first))
                    roundedFirstTimeStamp.minusMinutes(5)
                else
                    roundedFirstTimeStamp)
                    ?: return emptyList()


            val buckets = mutableListOf(firstBucket)
            while (buckets.last().isBefore(preparedData.last().first)) {
                buckets.add(buckets.last().plusMinutes(5))
            }

            val bucketData = mutableListOf<Pair<DateTime, Float?>>()

            var preparedDataIndex = 0

            buckets.forEach {
                val interval = Interval(it.minusSeconds(150), it.plusSeconds(150))

                var lastTimeStampWasInBucket = false
                val bucketTemperatures = mutableListOf<Float>()

                for (i in preparedDataIndex until preparedData.size) {
                    if (interval.contains(preparedData[preparedDataIndex].first)) {
                        bucketTemperatures.add(preparedData[preparedDataIndex].second)
                        lastTimeStampWasInBucket = true
                        preparedDataIndex = i
                    } else {
                        // if the previous timestamp was in the bucket, but this one isn't, than
                        // the remaining time stamps will also not be in the bucket. Skip to next
                        // bucket.
                        if (lastTimeStampWasInBucket) {
                            bucketData.add(it to bucketTemperatures.sum() / bucketTemperatures.size)
                            return@forEach
                        }
                    }
                }
                if (bucketTemperatures.isEmpty()) {
                    bucketData.add(it to null)
                }

            }

            drawLineGraph(bucketData)

            return bucketData

        } catch (e: Exception) {
            debugText.value = e.message
            return listOf<Pair<DateTime, Float?>>()
        }
    }

    private fun drawLineGraph(data: List<Pair<DateTime, Float?>>) {
        Timber.d("LINE DATA")
        data.forEach {
            Timber.d("${it.first} ... ${it.second}")
        }
        data.forEach {
            Log.d("LINE DATA", "${it.first} ... ${it.second}")
        }
    }

    private fun parseDateTime(dateTimeString: String): DateTime {
        val df: DateTimeFormatter = DateTimeFormat.forPattern(ISO_8601_DATE_FORMAT)
        return df.withOffsetParsed().parseDateTime(dateTimeString)
    }

    fun convertToLocalTime(dateTime: DateTime): DateTime {
        val localTimeZoneOffsetToUtc =
            Calendar.getInstance().timeZone.getOffset(dateTime.millis)

        return dateTime.withZone(DateTimeZone.forOffsetMillis(localTimeZoneOffsetToUtc))
    }

    companion object {
        const val ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
    }
}
