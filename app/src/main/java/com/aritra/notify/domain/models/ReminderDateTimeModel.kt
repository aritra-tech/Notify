package com.aritra.notify.domain.models

import java.time.LocalDateTime

enum class ReminderDateTimeModel(val dateTime: LocalDateTime = LocalDateTime.now()) {
    AFTER_30_MINUTES(
        dateTime = LocalDateTime.now().plusMinutes(30)
    ),
    AFTER_1_HOUR(dateTime = LocalDateTime.now().plusHours(1)),
    TOMORROW_MORNING_7(dateTime = LocalDateTime.now().withHour(7).plusDays(1)),
    CUSTOM ;


}