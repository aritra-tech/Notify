package com.aritra.notify.utils

import java.lang.StringBuilder
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

fun LocalDateTime.formatReminderDateTime(): String {
    val currentDateTime = LocalDateTime.now()
    val calcDays = dayOfMonth - currentDateTime.dayOfMonth
    val buildString = StringBuilder()
    when {
        calcDays == 1 -> {
            buildString.append("Tomorrow , ")
            buildString.append(format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
        }

        calcDays == 0 -> {
            buildString.append("Today , ")
            buildString.append(format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
        }

        else -> {
            buildString.append(format(DateTimeFormatter.ofPattern("dd MMM")))
                .append(" , ")
                .append(format(DateTimeFormatter.ofPattern("hh:mm a")))
        }
    }
    return buildString.toString()
}

fun LocalDateTime.checkDateIsNotOld(): Boolean {
    val currentDateTime = LocalDateTime.now()
    return (currentDateTime.dayOfMonth - dayOfMonth) <= 0
}

fun LocalDateTime.checkTimeIsNotOld(): Boolean {
    val currentDateTime = LocalTime.now()
    return currentDateTime.until(this.toLocalTime(), ChronoUnit.MILLIS) >= 0
}

fun LocalDateTime.triggerDateTime(): Long {
    val currentDateTime = LocalDateTime.now()
    return currentDateTime.until(this, ChronoUnit.MILLIS)
}
