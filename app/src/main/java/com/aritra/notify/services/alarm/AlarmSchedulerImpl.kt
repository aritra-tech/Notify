package com.aritra.notify.services.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import com.aritra.notify.utils.getNoteAlarmPendingIntent
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(private val context: Context) : AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    init {
        checkAlarm()
    }

    override fun scheduleAlarm(alarmInfo: AlarmInfo) {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + alarmInfo.triggerMillis,
            getNoteAlarmPendingIntent(context, alarmInfo.noteId)!!
        )
    }

    override fun cancelAlarm(alarmInfo: AlarmInfo) {
        getNoteAlarmPendingIntent(context, alarmInfo.noteId, PendingIntent.FLAG_NO_CREATE)?.let {
            alarmManager.cancel(it)
        }
    }

    override fun editScheduleAlarm(alarmInfo: AlarmInfo) {
        // TODO: improve later it is better tp check if pending Intent sent by using  Flag_NO_CREATE if return null it means not set pending intent by note id
        cancelAlarm(alarmInfo)
        scheduleAlarm(alarmInfo)
    }

    private fun checkAlarm() {
        if (Build.VERSION.SDK_INT >= 31) {
            if (alarmManager.canScheduleExactAlarms()) {
                // NO OP for now
            } else {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
                context.startActivity(intent)
            }
        } else {
            // NO OP for now
        }
    }
}
