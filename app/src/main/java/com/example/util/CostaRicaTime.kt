package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object CostaRicaTime {
    private val timeZone: TimeZone = TimeZone.getTimeZone("America/Costa_Rica")

    fun getCurrentFormattedTime(): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.US).apply {
            timeZone = CostaRicaTime.timeZone
        }
        return sdf.format(Date())
    }

    fun getCurrentHour(): Int {
        val calendar = Calendar.getInstance(timeZone)
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getGreeting(): String {
        val hour = getCurrentHour()
        return when {
            hour < 12 -> "GOOD MORNING"
            hour < 18 -> "GOOD AFTERNOON"
            else -> "GOOD EVENING"
        }
    }

    fun getTodayDateKey(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = CostaRicaTime.timeZone
        }
        return sdf.format(Date())
    }

    fun getYesterdayDateKey(): String {
        val calendar = Calendar.getInstance(timeZone).apply {
            add(Calendar.DAY_OF_YEAR, -1)
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
            timeZone = CostaRicaTime.timeZone
        }
        return sdf.format(calendar.time)
    }

    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("MMM d, yyyy", Locale.US).apply {
            timeZone = CostaRicaTime.timeZone
        }
        return sdf.format(Date())
    }
}
