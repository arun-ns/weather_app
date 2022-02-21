package com.sunnyday.model

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class WeatherData : Serializable {
    var day: String? = null
    var description: String? = null
    var sunrise = 0
    var sunset = 0
    var chance_rain = 0.0f
    var high = 0
    var low = 0
    var image: String? = null

    fun getDayString(isFullDay: Boolean): String {
        val wDay = day!!.toInt()
        if (wDay == 1) {
            return "Today"
        }
        val c = Calendar.getInstance()
        c.time = Date(System.currentTimeMillis())
        c.add(Calendar.DATE, wDay - 1)
        val f = if (isFullDay) {
            SimpleDateFormat(if (wDay > 7) "EEEE dd" else "EEEE", Locale.getDefault())
        } else {
            SimpleDateFormat(if (wDay > 7) "EE dd" else "EE", Locale.getDefault())
        }
        return f.format(c.time) + if (wDay > 7) getDayNumberSuffix(c.get(Calendar.DAY_OF_MONTH)) else ""
    }

    private fun getDayNumberSuffix(day: Int): String {
        return if (day in 11..13) {
            "th"
        } else when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

}
