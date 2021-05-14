package com.crazy.crazyalarm.clockUtils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.io.Serializable
import java.util.*

object AlarmManagerUtil {
    const val ALARM_ACTION = "com.crazy.crazyalarm.clock"
    // To put and get parameter more easily
    const val INTERVALLMILLIS: String = "intervalMillis"
    const val MSG: String = "msg"
    const val NOTICEFLAG: String = "soundOrVibrator"
    const val ID: String = "id"
    const val DayInMillis: Long = 86400000L
    const val WeekInMillis: Long= 604800000L

    fun setAlarmTime(context: Context, timeInMillis: Long, intent: Intent) {
        val am: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val sender: PendingIntent = PendingIntent.getBroadcast(context,
            intent.getIntExtra(ID, 0),
            intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val interval = intent.getLongExtra(INTERVALLMILLIS, 0)
        am.setWindow(AlarmManager.RTC_WAKEUP, timeInMillis, interval, sender)
    }
    fun cancelAlarm(context: Context, action: String, id: Int) {
        val intent = Intent(action)
        val pi: PendingIntent = PendingIntent.getBroadcast(context, id,
            intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val am: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(pi)
    }

    sealed class CycleFlag
    object Once : CycleFlag()
    object Daily : CycleFlag()
    object Weekly : CycleFlag()
    sealed class NoticeFlag:Serializable
    object OnlySound : NoticeFlag()
    object OnlyVibrator : NoticeFlag()
    object BothSoundAndVibrator : NoticeFlag()
    /**
     * @param flag 周期性表示，Once表示一次性闹钟， Daily表示每天重复， Weekly表示每周重复
     * @param hour 时
     * @param minute 分
     * @param
     */
    fun setAlarm(
        context: Context,
        hour: Int,
        minute: Int,
        flag: CycleFlag,
        prompt: String,
        id: Int,
        noticeFlag: NoticeFlag,
        week: Int
    ) {
        val am: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar:Calendar  = Calendar.getInstance()
        var intervalMillis: Long = 0
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH), hour, minute, 10)
        intervalMillis =  when(flag) {
            is Once-> 0
            is Daily-> DayInMillis
            is Weekly-> WeekInMillis
        }
        val intent = Intent(ALARM_ACTION)
        intent.putExtra(INTERVALLMILLIS, intervalMillis)
        intent.putExtra(MSG, prompt)
        intent.putExtra(ID, id)
        intent.putExtra(NOTICEFLAG, noticeFlag)
        val sender = PendingIntent.getBroadcast(context, id,
            intent, PendingIntent.FLAG_CANCEL_CURRENT)
        am.setWindow(
            AlarmManager.RTC_WAKEUP,
            calMethod(week, calendar.timeInMillis), intervalMillis, sender)
    }

    /**
     * @param weekFlag 周几
     * @param dateTime 时间戳(当天年月日+时分秒)
     * @return 闹钟起始时间的时间戳
     */
    private fun calMethod(weekFlag: Int, dateTime: Long): Long {
        var time: Long = 0
        // 重复性闹钟
        if (weekFlag != 0){
            val c = Calendar.getInstance()
            var week: Int = c.get(Calendar.DAY_OF_WEEK)
            week = when(week){
                1->7
                2->1
                3->2
                4->3
                5->4
                6->5
                7->6
                else-> week
            }
            // the alarm day is today
            if (weekFlag == week) {
                if (dateTime > System.currentTimeMillis()){
                    time = dateTime;
                } else {
                    time = dateTime + WeekInMillis
                }
                // the day hasn't reach
            }else if (weekFlag > week) {
                time = dateTime + (weekFlag - week)* DayInMillis
                // the day has reached
            }else if (weekFlag < week) {
                time = dateTime + (weekFlag - week) * DayInMillis + WeekInMillis
            }
        } else {
            // 一次性闹钟
            time = if (dateTime > System.currentTimeMillis()) {
                dateTime
            }else {
                dateTime + DayInMillis
            }
        }
        return time
    }
}