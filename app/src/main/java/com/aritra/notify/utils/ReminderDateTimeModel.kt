package com.aritra.notify.utils

import java.time.LocalDateTime

enum class ReminderDateTimeModel {
    AFTER_30_MINUTES,
    AFTER_1_HOUR,
    TOMORROW_MORNING_7,
    CUSTOM,
}

fun ReminderDateTimeModel.formatToLocalDateTime(): LocalDateTime {
    return when (this) {
        ReminderDateTimeModel.AFTER_30_MINUTES -> LocalDateTime.now().plusMinutes(30)
        ReminderDateTimeModel.AFTER_1_HOUR -> LocalDateTime.now().plusHours(1)
        ReminderDateTimeModel.TOMORROW_MORNING_7 -> LocalDateTime.now().withHour(7).withMinute(0).plusDays(1)
        ReminderDateTimeModel.CUSTOM -> LocalDateTime.now()
    }
}
