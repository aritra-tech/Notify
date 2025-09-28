package com.aritradas.notify.services.alarm

interface AlarmScheduler {
    fun scheduleAlarm(alarmInfo: AlarmInfo)
    fun cancelAlarm(alarmInfo: AlarmInfo)
    fun editScheduleAlarm(alarmInfo: AlarmInfo)
}

data class AlarmInfo(
    val noteId: Int,
    val triggerMillis: Long,
)
