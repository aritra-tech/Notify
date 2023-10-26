package com.aritra.notify.core.alarm

interface AlarmScheduler {
    fun scheduleAlarm(alarmInfo: AlarmInfo)
    fun cancelAlarm(alarmInfo: AlarmInfo)

}


data class AlarmInfo(
    val noteId:Int,
    val triggerMillis:Long
)